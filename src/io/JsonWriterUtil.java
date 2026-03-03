package io;
import game.*;
import model.User;
import pieces.*;
import org.json.simple.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class JsonWriterUtil {

    public static void writeUsers(List<User> users, String path) {
        JSONArray mainList = new JSONArray();

        for (User u : users) {
            Map<String, Object> orderedObj = new LinkedHashMap<>();

            orderedObj.put("email", u.getEmail());
            orderedObj.put("password", u.getPassword());
            orderedObj.put("points", u.getPoints());

            JSONArray gamesArr = new JSONArray();
            if (u.getGames() != null)
                for (Game g : u.getGames())
                    gamesArr.add(g.getId());

            orderedObj.put("games", gamesArr);
            mainList.add(orderedObj);
        }

        try (FileWriter file = new FileWriter(path)) {
            String prettyJson = formatJson(JSONValue.toJSONString(mainList));
            file.write(prettyJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // scriem games
    @SuppressWarnings("unchecked")
    public static void writeGames(Map<Integer, Game> games, String path) {
        JSONArray list = new JSONArray();

        for (Game g : games.values()) {
            Map<String, Object> orderedGame = new LinkedHashMap<>();
            orderedGame.put("id", g.getId());

            // scriem playeri
            JSONArray players = new JSONArray();
            if(g.getPlayer() != null) {
                Map<String, Object> p = new LinkedHashMap<>();
                p.put("email", g.getPlayer().getName());
                p.put("color", g.getPlayer().getColor().toString());
                players.add(p);
            }
            if(g.getComputer() != null) {
                Map<String, Object> c = new LinkedHashMap<>();
                c.put("email", "computer");
                c.put("color", g.getComputer().getColor().toString());
                players.add(c);
            }
            orderedGame.put("players", players);
            orderedGame.put("currentPlayerColor", g.getIndex() == 0 ? "WHITE" : "BLACK");

            // scriem si boardul
            JSONArray boardArr = new JSONArray();
            for (var entry : g.getBoard().getChessBoard()) {
                Piece p = entry.getValue();
                Map<String, Object> pObj = new LinkedHashMap<>();
                pObj.put("type", String.valueOf(p.type()));
                pObj.put("color", p.getColor().toString());
                pObj.put("position", p.getPosition().toString());
                boardArr.add(pObj);
            }
            orderedGame.put("board", boardArr);

            JSONArray movesArr = new JSONArray();
            if (g.getMoves() != null) {
                for (Move m : g.getMoves()) {
                    Map<String, Object> moveObj = new LinkedHashMap<>();

                    moveObj.put("playerColor", m.getColor().toString());
                    moveObj.put("from", m.getFrom().toString());
                    moveObj.put("to", m.getTo().toString());

                    if (m.getCaptured() != null) {
                        Map<String, Object> capturedObj = new LinkedHashMap<>();
                        capturedObj.put("type", String.valueOf(m.getCaptured().type()));
                        capturedObj.put("color", m.getCaptured().getColor().toString());
                        moveObj.put("captured", capturedObj);
                    }
                    movesArr.add(moveObj);
                }
            }
            orderedGame.put("moves", movesArr);
            list.add(orderedGame);
        }

        try (FileWriter file = new FileWriter(path)) {
            String prettyJson = formatJson(JSONValue.toJSONString(list));
            file.write(prettyJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // functie pentru afisare in fisier
    private static String formatJson(String json) {
        StringBuilder pretty = new StringBuilder();
        int indentLevel = 0;
        boolean inQuote = false;

        Stack<Boolean> compactModeStack = new Stack<>();
        compactModeStack.push(false);

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

            switch (c) {
                case '"':
                    inQuote = !inQuote;
                    pretty.append(c);
                    break;
                case ' ':
                    if (inQuote) pretty.append(c);
                    break;
                case '{':
                    pretty.append(c);
                    if (!inQuote) {
                        pretty.append("\n");
                        indentLevel++;
                        addIndent(pretty, indentLevel);
                    }
                    break;
                case '}':
                    if (!inQuote) {
                        pretty.append("\n");
                        indentLevel--;
                        addIndent(pretty, indentLevel);
                    }
                    pretty.append(c);
                    break;
                case '[':
                    pretty.append(c);
                    if (!inQuote) {
                        boolean isSimple = isSimpleArray(json, i);
                        if (isSimple) {
                            compactModeStack.push(true);
                        } else {
                            compactModeStack.push(false);
                            pretty.append("\n");
                            indentLevel++;
                            addIndent(pretty, indentLevel);
                        }
                    }
                    break;
                case ']':
                    if (!inQuote) {
                        boolean isCompact = compactModeStack.pop();
                        if (!isCompact) {
                            pretty.append("\n");
                            indentLevel--;
                            addIndent(pretty, indentLevel);
                        }
                    }
                    pretty.append(c);
                    break;
                case ',':
                    pretty.append(c);
                    if (!inQuote) {
                        boolean isCompact = !compactModeStack.isEmpty() && compactModeStack.peek();
                        if (isCompact) {
                            pretty.append(" ");
                        } else {
                            pretty.append("\n");
                            addIndent(pretty, indentLevel);
                        }
                    }
                    break;
                case ':':
                    pretty.append(c);
                    if (!inQuote) {
                        pretty.append(" ");
                    }
                    break;
                default:
                    pretty.append(c);
            }
        }
        return pretty.toString();
    }

    private static boolean isSimpleArray(String json, int startIndex) {
        for (int k = startIndex + 1; k < json.length(); k++) {
            char ch = json.charAt(k);
            if (ch == '{' || ch == '[') return false;
            if (ch == ']') return true;
        }
        return false;
    }

    private static void addIndent(StringBuilder sb, int level) {
        for (int i = 0; i < level; i++)
            sb.append("  ");
    }
}