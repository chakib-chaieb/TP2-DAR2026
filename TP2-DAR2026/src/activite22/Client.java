package activite22;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Activité 2-2 : Client TCP - envoie un objet Operation et affiche le résultat.
 */
public class Client {

    private static final String HOST = "localhost";
    private static final int PORT = 1234;

    private static final Pattern OPERATION = Pattern.compile(
            "^\\s*(-?\\d+(?:\\.\\d+)?)\\s*([+\\-*/])\\s*(-?\\d+(?:\\.\\d+)?)\\s*$");

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             Scanner sc = new Scanner(System.in)) {

            // IMPORTANT : même ordre que le serveur (Output d'abord, puis Input)
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            System.out.println("[Client] Connecté au serveur " + HOST + ":" + PORT);
            while (true) {
                System.out.print("Entrez une opération (ex: 55 * 25) ou 'quit' : ");
                if (!sc.hasNextLine()) {
                    break;
                }
                String saisie = sc.nextLine();
                if (saisie.trim().equalsIgnoreCase("quit")) {
                    break;
                }
                Matcher m = OPERATION.matcher(saisie);
                if (!m.matches()) {
                    System.out.println("Syntaxe invalide. Format attendu : <nombre> <+|-|*|/> <nombre>");
                    continue;
                }
                Operation op = new Operation(
                        Double.parseDouble(m.group(1)),
                        m.group(2).charAt(0),
                        Double.parseDouble(m.group(3)));

                out.writeObject(op);                 // b) envoi de l'objet
                out.flush();

                Object resultat = in.readObject();   // g) lecture du résultat
                if (resultat instanceof Double) {
                    double r = (Double) resultat;
                    if (r == Math.rint(r) && Math.abs(r) < 1e15) {
                        System.out.println("Résultat : " + (long) r);
                    } else {
                        System.out.println("Résultat : " + r);
                    }
                } else {
                    System.out.println(resultat);
                }
            }
            System.out.println("[Client] Déconnexion.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[Client] Erreur : " + e.getMessage());
        }
    }
}
