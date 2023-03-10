package com.dbc.view;

import com.dbc.exceptions.ValorDeEntradaException;
import com.dbc.model.Cliente;
import com.dbc.service.ClienteService;
import com.dbc.service.ConvenioService;
import com.dbc.service.UsuarioService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ClienteView {
    public static void exibirMenu() {

        ClienteService clienteService = new ClienteService();
        UsuarioService usuarioService = new UsuarioService();
        ConvenioService convenioService = new ConvenioService();
        int opcao = -1;

        try {
            Scanner scanner = new Scanner(System.in);
            while (opcao != 0) {
                System.out.println("Digite 1 para criar Cliente");
                System.out.println("Digite 2 para listar Cliente");
                System.out.println("Digite 3 para editar Cliente");
                System.out.println("Digite 4 para excluir Cliente");
                System.out.println("Digite 0 para sair");
                opcao = scanner.nextInt();
                scanner.nextLine();
                switch (opcao) {

                    case 1: {
                        // adição Cliente
                        Cliente cliente = new Cliente();

                        usuarioService.listar();
                        System.out.println("Digite o código de Usuário:");
                        cliente.setIdUsuario(scanner.nextInt());
                        scanner.nextLine();

                        ValorEntrada.validarEntrada(cliente.getIdUsuario(), 1, 999999999);

                        if(usuarioService.verificarIdUsuario(cliente.getIdUsuario())){
                            throw new ValorDeEntradaException("Usuário já registrado para outro login.");
                        }

                        System.out.println("Cliente possui convênio? ('s' para confirmar)");
                        if ("s".equalsIgnoreCase(scanner.nextLine())) {
                            convenioService.listar();
                            System.out.println("Escolha um convênio: ");
                            cliente.setIdConvenio(scanner.nextInt());
                            scanner.nextLine();
                            ValorEntrada.validarEntrada(cliente.getIdConvenio(), 1, 999999999);

                        }

                        clienteService.adicionar(cliente);
                        break;
                    }
                    case 2: {
                        // listagem
                        clienteService.listar();
                        break;
                    }
                    case 3: {
                        // edição
                        System.out.println("Qual cliente você deseja editar?");
                        clienteService.listar();
                        int id = scanner.nextInt();
                        ValorEntrada.validarEntrada(id, 1, 999999999);

                        Cliente cliente = new Cliente();

                        Integer contadorDeAlteracoes = 0;
                        System.out.println("Deseja trocar os dados para outro Usuário? ('s' para confirmar)");
                        if ("s".equalsIgnoreCase(scanner.nextLine())) {
                            contadorDeAlteracoes++;
                            usuarioService.listar();
                            System.out.println("Escolha o registro de Usuário: ");
                            cliente.setIdUsuario(scanner.nextInt());
                            scanner.nextLine();
                            ValorEntrada.validarEntrada(cliente.getIdUsuario(), 1, 999999999);

                            if(usuarioService.verificarIdUsuario(cliente.getIdUsuario())){
                                throw new ValorDeEntradaException("Usuário já registrado para outro login.");
                            }
                        }

                        System.out.println("Deseja trocar o Convênio? ('s' para confirmar)");
                        if ("s".equalsIgnoreCase(scanner.nextLine())) {
                            contadorDeAlteracoes++;
                            convenioService.listar();
                            System.out.println("Escolha o registro de convênio: ");
                            cliente.setIdConvenio(scanner.nextInt());
                            scanner.nextLine();
                            ValorEntrada.validarEntrada(cliente.getIdConvenio(), 1, 999999999);
                        }

                        if (contadorDeAlteracoes == 0) {
                            System.err.println("Nenhuma alteração foi feita.");
                            break;
                        }

                        clienteService.editar(id, cliente);
                        break;
                    }
                    case 4: {
                        // exclusão
                        System.out.println("Qual cliente você deseja excluir?");
                        clienteService.listar();

                        int id = scanner.nextInt();
                        scanner.nextLine();
                        clienteService.remover(id);

                        ValorEntrada.validarEntrada(id, 1, 999999999);

                        break;
                    }
                    case 0:
                        break;
                    default:
                        System.err.println("opção inválida");
                        break;
                }
            }
        } catch (InputMismatchException e) {
            e.printStackTrace();
            exibirMenu();
        }
    }
}
