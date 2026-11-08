package activite22;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Activité 2-2 : Serveur TCP - reçoit un objet Operation, calcule, renvoie le résultat.
 */
public class Serveur {

    public static final int PORT = 1234;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[Serveur] En attente de connexion sur le port " + PORT + "...");
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("[Serveur] Client connecté : " + socket.getInetAddress());

                    // IMPORTANT : créer et flusher l'ObjectOutputStream AVANT l'ObjectInputStream
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.flush();
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                    while (true) {
                        Object recu;
                        try {
                            recu = in.readObject();                  // c) réception de l'objet
                        } catch (EOFException e) {
                            break;                                   // le client a fermé la connexion
                        }
                        if (!(recu instanceof Operation)) {
                            out.writeObject("Erreur : objet reçu invalide");
                            out.flush();
                            continue;
                        }
                        Operation op = (Operation) recu;
                        System.out.println("[Serveur] Reçu : " + op);   // d) récupération des données

                        Object resultat = calculer(op);                 // e) calcul
                        System.out.println("[Serveur] Envoi : " + resultat);
                        out.writeObject(resultat);                      // f) renvoi du résultat
                        out.flush();
                    }
                    System.out.println("[Serveur] Client déconnecté.");
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("[Serveur] Erreur de communication : " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("[Serveur] Impossible de démarrer : " + e.getMessage());
        }
    }

    /** Retourne un Double si tout va bien, sinon un String décrivant l'erreur. */
    static Object calculer(Operation op) {
        double x = op.getOperande1();
        double y = op.getOperande2();
        switch (op.getOperateur()) {
            case '+': return x + y;
            case '-': return x - y;
            case '*': return x * y;
            case '/':
                if (y == 0) {
                    return "Erreur : division par zéro";
                }
                return x / y;
            default:
                return "Erreur : opérateur invalide";
        }
    }
}
