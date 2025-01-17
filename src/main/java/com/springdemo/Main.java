package com.springdemo;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.ArrayList;

public class Main {
    public static ArrayList<Contato> listaContatos = new ArrayList<Contato>();

    public static void main(String[] args) {
        Spark.port(8080);
        System.out.println("Conexao com a porta 8080 funcionando!");

        Spark.post("/contato/criar", criarContato());
        Spark.get("/contato/listarTodosContatos", listarTodosContatos());
        //Spark.put("/contato/:cpf", atualizarContato());
        Spark.delete("contato/excluir", excluirContato());
        Spark.put("/contato/atualizar", atualizarContato());
        /*Spark.get("/contato/obterPorCpf/:cpf", obterPorCpf());
        Spark.get("/contato/obterPorIdade/:idade", obterPorIdade());*/

    }

    private static Route criarContato(){
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                String name = request.queryParams("nome");
                String age= request.queryParams("idade");
                String cpf = request.queryParams("cpf");

                Contato novoContato = new Contato(name, age, cpf);
                listaContatos.add(novoContato);

                System.out.println(listaContatos);

                response.status(201);
                return "Contato adicionado com sucesso!";

            }
        };
    }

    private static Route listarTodosContatos(){
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                String resposta = "";

                if (listaContatos.isEmpty()) {
                    resposta += "Nenhum contato cadastrado";
                } else {
                    resposta = "\n-- Lista de Contatos --";
                    for (Contato cadaContato : listaContatos) {
                        resposta += "\n" + cadaContato.toString();
                    }
                }
                return resposta;
            }
        };
    }

    private static Route atualizarContato( ){
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                //String cpf = request.params(":cpf");
                String cpf = request.queryParams("cpf");
                String nome = request.queryParams("nome");
                String idade = request.queryParams("idade");

                if (listaContatos.isEmpty()) {
                    response.status(404);
                    return "Não existe esse contato na base de dados";
                }

                for (Contato cadaContato : listaContatos) {
                    if (cadaContato.getCpf().equals(cpf)) {
                        cadaContato.setNome(nome);
                        cadaContato.setIdade(idade);

                        response.status(200);
                        return "Contato com CPF " + cpf + " foi atualizado.";
                    }
                }

                response.status(404);
                return "Contato com cpf: " + cpf + "não encontrado.";
            }
        };
    }

    private static Route excluirContato(){
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {

                String cpf = request.queryParams("cpf");

                if (listaContatos.isEmpty()) {
                    response.status(404);
                    return "Não existem contatos na base de dados.";
                } else {
                    for (Contato cadaContato : listaContatos) {
                        if (cadaContato.getCpf().equals(cpf)) {
                            listaContatos.remove(cadaContato);

                            response.status(200);
                            return "Contato com cpf:" + cpf + "foi excluido com sucesso!";
                        }
                    }
                }


                response.status(404);
                return "Contato com cpf:" + cpf + "não foi encontrado";
            }
        };
    }
}
