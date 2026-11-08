package activite21;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Activité 2-1 : Serveur TCP - échange de chaînes de caractères (CC).
 * Reçoit 3 lignes (operande1, operateur, operande2), calcule et renvoie le résultat.
 */
public class Serveur {

    public static final int PORT = 1234;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[Serveur] En attente de connexion sur le port " + PORT + "...");
            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    System.out.println("[Serveur] Client connecté : " + socket.getInetAddress());
                    String op1;
                    while ((op1 = in.readLine()) != null) {
                        String operateur = in.readLine();
                        String op2 = in.readLine();
                        if (operateur == null || op2 == null) {
                            break;
                        }
                        System.out.println("[Serveur] Reçu : " + op1 + " " + operateur + " " + op2);
                        String resultat = calculer(op1, operateur, op2);
                        System.out.println("[Serveur] Envoi : " + resultat);
                        out.println(resultat);
                    }
                    System.out.println("[Serveur] Client déconnecté.");
                } catch (IOException e) {
                    System.err.println("[Serveur] Erreur de communication : " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("[Serveur] Impossible de démarrer : " + e.getMessage());
        }
    }

    static String calculer(String a, String operateur, String b) {
        try {
            double x = Double.parseDouble(a);
            double y = Double.parseDouble(b);
            double r;
            switch (operateur) {
                case "+": r = x + y; break;
                case "-": r = x - y; break;
                case "*": r = x * y; break;
                case "/":
                    if (y == 0) {
                        return "Erreur : division par zéro";
                    }
                    r = x / y;
                    break;
                default:
                    return "Erreur : opérateur invalide";
            }
            if (r == Math.rint(r) && Math.abs(r) < 1e15) {
                return String.valueOf((long) r);
            }
            return String.valueOf(r);
        } catch (NumberFormatException e) {
            return "Erreur : opérande invalide";
        }
    }
}
