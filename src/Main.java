import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.Update;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.io.FileReader;
import java.text.SimpleDateFormat;

public class Main {

	// nome do app
	private static final String APPLICATION_NAME = "Google Drive";

	// Instanciar o JSON
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	// TOKEN das credenciais
	private static final String TOKENS_DIRECTORY_PATH = "tokens";

	/**
	 * Global instance of the scopes required by this quickstart. If modifying these
	 * scopes, delete your previously saved tokens/ folder.
	 */

	private static final List SCOPES = Collections.singletonList(DriveScopes.DRIVE);
	// private static final List<String> SCOPES =
	// Collections.singletonList(DriveScopes.DRIVE_METADATA_READONLY);

	// credenciais
	// private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @param HTTP_TRANSPORT The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException If the credentials.json file cannot be found.
	 */

	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Carregar Credenciais

		InputStream in = Main.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Arquivo não encontrado: " + CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

	public static void main(String... args) throws IOException, GeneralSecurityException {

		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME).build();

		// Printar o nome e a ID dos primeiros 10 arquivos.

		int option = 6;


		Scanner sc = new Scanner(System.in);

		while (option == 6) {
			System.out.println("Bem vindo ao Google Drive!");
			System.out.println(" 1 - Listar todos os Arquivos.");
			System.out.println(" 2 - Upload de arquivo.");
			System.out.println(" 3 - Deletar arquivo.");
			System.out.println(" 4 - Sair.");
			option = sc.nextInt();
			switch (option) {
			case 1:
				System.out.println("Opção 1 selecionada.");
				option = 1;
				FileList result = service.files().list().setPageSize(10).setFields("nextPageToken, files(id, name)")
						.execute();
				List<File> files = result.getFiles();
				if (files == null || files.isEmpty()) {
					System.out.println("Nenhum arquivo encontrado.");
				} else {
					System.out.println("Arquivos:");
					for (File file : files) {
						System.out.printf("Nome do Arquivo: %s \n ID do Arquivo: (%s)\n\n", file.getName(), file.getId());
					}
				}
				break;
				
			case 2:
				System.out.println("Opção 2 selecionada.");
				option = 2;		
				
				String nomeArquivo;
				File fileMetadata = new File();
				//System.out.println(" Digite o nome do arquivo a ser salvo.");
				//nomeArquivo = sc.next();
				//fileMetadata.setName(nomeArquivo+".txt");

				
				java.io.File filePath = new java.io.File("D:\\tabuada.txt");
				FileContent mediaContent = new FileContent("arquivos/txt", filePath);
				File file = service.files().create(fileMetadata, mediaContent)
				    .setFields("id")
				    .execute();
				System.out.println("File ID: " + file.getId());
				
				
				System.out.println("Upload Com sucesso!");
				
			break;
				
			case 3:
				System.out.println("Opção 3 selecionada.");
				
				break;
				
			case 4:
				System.out.println("Opção 4 selecionada.");
				
				break;
			default:
				break;
			}
		}

	}
}