import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Scanner;

public class GerenciaBanco {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String nome;
        String sobrenome;
        String cpf;
        while (true) {
            System.out.print("Nome: ");
            nome = sc.nextLine().trim();
            if (!nome.isEmpty()) break;
            System.out.println("Entrada inválida.");
        }
        while (true) {
            System.out.print("Sobrenome: ");
            sobrenome = sc.nextLine().trim();
            if (!sobrenome.isEmpty()) break;
            System.out.println("Entrada inválida.");
        }
        while (true) {
            System.out.print("CPF (11 dígitos, apenas números): ");
            cpf = sc.nextLine().replaceAll("\\D", "");
            if (cpf.length() == 11) break;
            System.out.println("CPF inválido.");
        }
        Pessoa titular = new Pessoa(nome, sobrenome, cpf);
        Conta conta = new Conta(titular);
        int opcao;
        do {
            opcao = exibirMenu(sc);
            switch (opcao) {
                case 1 -> {
                    System.out.println("Saldo atual: R$ " + conta.getSaldoFormatado());
                }
                case 2 -> {
                    System.out.print("Valor do depósito: ");
                    String entrada = sc.nextLine().trim().replace(",", ".");
                    try {
                        BigDecimal valor = new BigDecimal(entrada).setScale(2, RoundingMode.HALF_UP);
                        boolean ok = conta.depositar(valor);
                        if (ok) {
                            System.out.println("Depósito realizado. Saldo: R$ " + conta.getSaldoFormatado());
                        } else {
                            System.out.println("Valor inválido para depósito.");
                        }
                    } catch (Exception e) {
                        System.out.println("Entrada inválida.");
                    }
                }
                case 3 -> {
                    System.out.print("Valor do saque: ");
                    String entrada = sc.nextLine().trim().replace(",", ".");
                    try {
                        BigDecimal valor = new BigDecimal(entrada).setScale(2, RoundingMode.HALF_UP);
                        boolean ok = conta.sacar(valor);
                        if (ok) {
                            System.out.println("Saque realizado. Saldo: R$ " + conta.getSaldoFormatado());
                        } else {
                            System.out.println("Saque inválido. Verifique o valor e o saldo.");
                        }
                    } catch (Exception e) {
                        System.out.println("Entrada inválida.");
                    }
                }
                case 4 -> {
                    System.out.println("Titular: " + titular.getNomeCompleto());
                    System.out.println("CPF: " + titular.getCpfFormatado());
                }
                case 0 -> {
                    System.out.println("Obrigado por utilizar o gerenciaBanco. Até logo!");
                }
                default -> System.out.println("Opção inválida.");
            }
            if (opcao != 0) {
                System.out.println();
                System.out.print("Pressione ENTER para continuar...");
                sc.nextLine();
                System.out.println();
            }
        } while (opcao != 0);
        sc.close();
    }

    private static int exibirMenu(Scanner sc) {
        System.out.println("===== gerenciaBanco =====");
        System.out.println("[1] Consultar saldo");
        System.out.println("[2] Depositar");
        System.out.println("[3] Sacar");
        System.out.println("[4] Dados do titular");
        System.out.println("[0] Encerrar");
        System.out.print("Escolha: ");
        String entrada = sc.nextLine().trim();
        try {
            return Integer.parseInt(entrada);
        } catch (Exception e) {
            return -1;
        }
    }
}

class Pessoa {
    private final String nome;
    private final String sobrenome;
    private final String cpf;

    public Pessoa(String nome, String sobrenome, String cpf) {
        this.nome = Objects.requireNonNull(nome).trim();
        this.sobrenome = Objects.requireNonNull(sobrenome).trim();
        this.cpf = Objects.requireNonNull(cpf).replaceAll("\\D", "");
        if (this.nome.isEmpty() || this.sobrenome.isEmpty() || this.cpf.length() != 11) {
            throw new IllegalArgumentException("Dados pessoais inválidos");
        }
    }

    public String getNomeCompleto() {
        return nome + " " + sobrenome;
    }

    public String getCpfFormatado() {
        String c = cpf;
        return c.substring(0, 3) + "." + c.substring(3, 6) + "." + c.substring(6, 9) + "-" + c.substring(9);
    }
}

class Conta {
    private final Pessoa titular;
    private BigDecimal saldo;

    public Conta(Pessoa titular) {
        this.titular = Objects.requireNonNull(titular);
        this.saldo = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    public boolean depositar(BigDecimal valor) {
        if (valor == null) return false;
        if (valor.compareTo(BigDecimal.ZERO) <= 0) return false;
        saldo = saldo.add(valor).setScale(2, RoundingMode.HALF_UP);
        return true;
    }

    public boolean sacar(BigDecimal valor) {
        if (valor == null) return false;
        if (valor.compareTo(BigDecimal.ZERO) <= 0) return false;
        if (valor.compareTo(saldo) > 0) return false;
        saldo = saldo.subtract(valor).setScale(2, RoundingMode.HALF_UP);
        return true;
    }

    public String getSaldoFormatado() {
        return saldo.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}
