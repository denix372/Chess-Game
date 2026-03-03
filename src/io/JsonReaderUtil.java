package io;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.io.Reader;
import java.util.*;
import pieces.*;
import model.*;
import game.*;

public class JsonReaderUtil {

    private JsonReaderUtil() {
    }

    public static List<User> readUsers(Path path, Map<Integer, Game> existingGames) {
        List<User> result = new ArrayList<>();
        if (path == null || !Files.exists(path)) return result;

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JSONParser parser = new JSONParser();
            Object parsed = parser.parse(reader);

            JSONArray arr = asArray(parsed);
            if (arr == null) return result;

            for (Object item : arr) {
                JSONObject obj = asObject(item);
                if (obj == null) continue;

                String email = asString(obj.get("email"));
                String pass = asString(obj.get("password"));

                User user = new User(email, pass);

                long pts = asLong(obj.get("points"), 0L);
                user.setPoints((int) pts);

                JSONArray gamesArr = asArray(obj.get("games"));
                if (gamesArr != null) {
                    for (Object gidObj : gamesArr) {
                        int gid = asInt(gidObj, -1);
                        Game gameObj = existingGames.get(gid);

                        if (gameObj != null)
                            user.addGame(gameObj);
                    }
                }
                result.add(user);
            }
        } catch (Exception e) {
            System.err.println("Eroare la citirea userilor: " + e.getMessage());
        }
        return result;
    }

    public static Map<Integer, Game> readGames(Path path) {
        Map<Integer, Game> map = new HashMap<>();
        if (path == null || !Files.exists(path)) return map;

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JSONParser parser = new JSONParser();
            Object parsed = parser.parse(reader);

            JSONArray arr = asArray(parsed);
            if(arr == null) return map;

            for (Object item : arr) {
                JSONObject obj = asObject(item);
                if (obj == null) continue;

                long idLong = asLong(obj.get("id"), 0);
                int id = (int) idLong;

                Game g = new Game();
                g.setId(id);
                g.setActive(true);
                g.setPaused(true);

                // citim playerii
                JSONArray playersArr = asArray(obj.get("players"));
                if (playersArr != null) {
                    for (Object pItem : playersArr) {
                        JSONObject pObj = asObject(pItem);
                        if (pObj == null) continue;

                        String email = asString(pObj.get("email"));
                        if (email == null) continue;

                        String colorStr = asString(pObj.get("color"));
                        Colors color = "WHITE".equals(colorStr) ? Colors.WHITE : Colors.BLACK;

                        Player p = new Player(email, color);
                        if (email.equalsIgnoreCase("computer")) {
                            g.setComputer(p);
                        } else {
                            g.setPlayer(p);
                        }
                    }
                }
                // daca nu s-a setat computer din JSON, punem default
                if(g.getComputer() == null)
                    g.setComputer(new Player("Computer", Colors.BLACK));

                // citim board-ul
                JSONArray boardArr = asArray(obj.get("board"));
                if (boardArr != null) {
                    Board board = new Board();
                    board.getChessBoard().clear();

                    for (Object bItem : boardArr) {
                        JSONObject bObj = asObject(bItem);
                        if (bObj == null)
                            continue;

                        String type = asString(bObj.get("type"));
                        String colorStr = asString(bObj.get("color"));
                        String posStr = asString(bObj.get("position"));

                        Colors col = "WHITE".equals(colorStr) ? Colors.WHITE : Colors.BLACK;
                        Position pos = new Position(posStr);
                        Piece piece = createPiece(type.charAt(0), col, pos);

                        if (piece != null) {
                            board.getChessBoard().add(new ChessPair<>(pos, piece));
                        }
                    }
                    g.setBoard(board);

                    if(g.getPlayer() != null)
                        g.getPlayer().setOwnedPieces(board.getChessBoard());
                    if(g.getComputer() != null)
                        g.getComputer().setOwnedPieces(board.getChessBoard());

                    // verificam daca pionii sunt pe pozitiilor lor, daca nu, nu au dreptul la a doua mutare
                    for (ChessPair<Position, Piece> pair : board.getChessBoard()) {
                        Piece p = pair.getValue();
                        if (p instanceof Pawn) {
                            Position pos = p.getPosition();
                            if (p.getColor() == Colors.WHITE && pos.getY() != 2) {
                                ((Pawn) p).setFirstmove(1);
                            } else if (p.getColor() == Colors.BLACK && pos.getY() != 7) {
                                ((Pawn) p).setFirstmove(1);
                            }
                        }
                    }
                }

                // citim culoarea playerului curent
                String currentTurn = asString(obj.get("currentPlayerColor"));
                if (currentTurn != null) {
                    if (currentTurn.equals("WHITE")) g.setIndex(0);
                    else g.setIndex(1);
                }

                // citim mutarile si piesele capturate in urma acestora
                JSONArray movesArr = asArray(obj.get("moves"));
                if (movesArr != null) {
                    for (Object mItem : movesArr) {
                        JSONObject mObj = asObject(mItem);
                        if (mObj == null) continue;

                        String colorStr = asString(mObj.get("playerColor"));
                        String fromStr = asString(mObj.get("from"));
                        String toStr = asString(mObj.get("to"));

                        if (colorStr != null && fromStr != null && toStr != null) {
                            Colors color = "WHITE".equals(colorStr) ? Colors.WHITE : Colors.BLACK;
                            Position from = new Position(fromStr);
                            Position to = new Position(toStr);

                            Piece capturedPiece = null;
                            JSONObject capObj = asObject(mObj.get("captured"));
                            if (capObj != null) {
                                String cType = asString(capObj.get("type"));
                                String cColorStr = asString(capObj.get("color"));
                                if (cType != null && cColorStr != null) {
                                    Colors cColor = "WHITE".equals(cColorStr) ? Colors.WHITE : Colors.BLACK;
                                    capturedPiece = createPiece(cType.charAt(0), cColor, to);
                                }
                            }

                            Move move = new Move(color, from, to, capturedPiece);
                            g.getMoves().add(move);
                        }
                    }
                }

                map.put(id, g);
            }
        } catch (Exception e) {
            System.err.println("Eroare la citirea jocurilor: " + e.getMessage());
        }
        return map;
    }

    private static Piece createPiece(char type, Colors color, Position pos) {
        // 2. Am utilizator Factory pattern
        return PieceFactory.factory(type, color, pos);
    }

    private static JSONArray asArray(Object o) {
        return (o instanceof JSONArray) ? (JSONArray) o : null;
    }

    private static JSONObject asObject(Object o) {
        return (o instanceof JSONObject) ? (JSONObject) o : null;
    }

    private static String asString(Object o) {
        return o == null ? null : String.valueOf(o);
    }

    private static int asInt(Object o, int def) {
        if (o instanceof Number) return ((Number) o).intValue();
        try {
            return o != null ? Integer.parseInt(String.valueOf(o)) : def;
        } catch (NumberFormatException e) {
            return def;
        }
    }

    private static long asLong(Object o, long def) {
        if (o instanceof Number) return ((Number) o).longValue();
        try {
            return o != null ? Long.parseLong(String.valueOf(o)) : def;
        } catch (NumberFormatException e) {
            return def;
        }
    }
}