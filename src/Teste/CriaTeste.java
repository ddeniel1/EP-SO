package Teste;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import Principal.BCP;

public class CriaTeste {
	static public double mediaTrocas;
	static public double mediaInstrucoes;
	public static void main(String[] args) throws IOException {
		criarTestes();
		lerTestes();
		
		
	}

	private static void lerTestes() throws IOException {
		// TODO Auto-generated method stub
		File pastas[];
		File diretorio = new File("entradasTeste");
		pastas = diretorio.listFiles();
		for(int i = 0; i < pastas.length; i++){
			
		}
	}

	

	private static void criarTestes() throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		for (int i = 0; i < 10; i++) {

			int processos = (int) (Math.random() * 100);
			for (int j = 0; j < processos; j++) {
				new File("entradas_teste/" + i + "/").mkdir();
				PrintWriter writer = new PrintWriter("entradasTeste/" + i + "/" + (j < 9 ? "0" + j : j) + ".txt",
						"UTF-8");
				String s = "TESTE-" + j;
				writer.println(s);
				int instrucoes = (int) (Math.random() * 18) + 1;
				for (int k = 0; k < instrucoes; k++) {
					int instruc = (int) Math.floor(Math.random() * 3);
					switch (instruc) {
					case 0:
						writer.println("X=" + (int) (Math.random() * 30));
						break;
					case 1:
						writer.println("Y=" + (int) (Math.random() * 30));
						break;
					case 2:
						writer.println("E/S");
						break;
					default:
						break;
					}
				}
				writer.print("SAIDA");

				writer.close();
				writer = new PrintWriter("entradas_teste/" + i + "/prioridades.txt", "UTF-8");
				for (int k = 0; k < processos; k++) {
					writer.println((int) Math.ceil(Math.random() * instrucoes));
				}
				writer.close();
			}
		}
	}
}
