package activite21;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Activité 2-1 : Client TCP - envoie une opération complète (ex : 55 * 25).
 * La syntaxe est validée AVANT l'envoi (refuse "25 ** 55" ou "abc + 3").
 */
public class Client {

    private static final String HOST = "localhost";
    private static final int PORT = 1234;

    private static final Pattern OPERATION = Pattern.compile(
            "^\\s*(-?\\d+(?:\\.\\d+)?)\\s*([+\\-*/])\\s*(-?\\d+(?:\\.\\d+)?)\\s*$");

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner sc = new Scanner(System.in)) {

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
                out.println(m.group(1));   // premier opérande
                out.println(m.group(2));   // opérateur
                out.println(m.group(3));   // deuxième opérande

                String resultat = in.readLine();
                if (resultat == null) {
                    System.out.println("[Client] Le serveur a fermé la connexion.");
                    break;
                }
                System.out.println("Résultat : " + resultat);
            }
            System.out.println("[Client] Déconnexion.");
        } catch (IOException e) {
            System.err.println("[Client] Erreur : " + e.getMessage());
        }
    }
}
