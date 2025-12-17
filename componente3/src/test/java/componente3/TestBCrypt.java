package componente3;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestBCrypt {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Hash que estava no banco (problema)
        String hashProblema = "$2a$10$0zpSF1jzL8eQSjdES/VY/.13MjebazxmWwl7tB9lLrHqBci3MMUL.";
        String senhaCorreta = "Ronaldo02!";
        
        System.out.println("=== TESTE PARA REPRESENTANTE ===");
        System.out.println("Email: ronaldo@gmail.com");
        System.out.println("Senha correta: " + senhaCorreta);
        System.out.println("Hash problemático: " + hashProblema);
        System.out.println("=================================\n");
        
        // 1. Teste se o hash problemático corresponde à senha
        System.out.println("1. Testando hash problemático:");
        boolean corresponde = encoder.matches(senhaCorreta, hashProblema);
        System.out.println("   '" + senhaCorreta + "' corresponde ao hash? " + corresponde);
        
        if (!corresponde) {
            System.out.println("   ✗ NÃO CORRESPONDE - Hash foi gerado com outra senha!");
            
            // Teste variações comuns
            System.out.println("\n2. Testando variações:");
            String[] variacoes = {
                "Ronaldo02!",      // original
                "Ronaldo02",       // sem !
                "ronaldo02!",      // r minúsculo
                "Ronaldo02@",      // @ em vez de !
                "Ronaldo02#",      // # em vez de !
                "Ronaldo02! ",     // com espaço
                "Ronaldo 02!",     // com espaço no meio
                "Ronaldo02.",      // . em vez de !
                "Ronaldo02?",      // ? em vez de !
                "Ronaldo02!1",     // com número extra
                "Ronaldo021!",     // números trocados
            };
            
            for (String var : variacoes) {
                boolean match = encoder.matches(var, hashProblema);
                if (match) {
                    System.out.println("   ✓ ENCONTRADO: Hash foi gerado com '" + var + "'");
                }
            }
        }
        
        // 3. Gerar novo hash correto
        System.out.println("\n3. Gerando novo hash correto:");
        String novoHash = encoder.encode(senhaCorreta);
        System.out.println("   Novo hash para '" + senhaCorreta + "':");
        System.out.println("   " + novoHash);
        
        // 4. Verificar que funciona
        boolean verificado = encoder.matches(senhaCorreta, novoHash);
        System.out.println("\n4. Verificação do novo hash:");
        System.out.println("   Senha '" + senhaCorreta + "' verifica com novo hash? " + verificado);
        
        if (verificado) {
            System.out.println("   ✓ NOVO HASH FUNCIONA CORRETAMENTE!");
            
            // 5. Comando SQL para inserir
            System.out.println("\n5. COMANDO SQL PARA INSERIR NOVO REPRESENTANTE:");
            
            // Primeiro, precisamos de uma empresa existente
            System.out.println("\n   Primeiro, encontre uma empresa_id válida:");
            System.out.println("   SELECT id, nome FROM empresa LIMIT 5;");
            
            System.out.println("\n   Depois, execute (ajuste empresa_id):");
            System.out.println("   INSERT INTO representante_empresa (email, password, nome, cargo, telefone, empresa_id, data_criacao)");
            System.out.println("   VALUES (");
            System.out.println("     'ronaldo@gmail.com',");
            System.out.println("     '" + novoHash + "',");
            System.out.println("     'Ronaldo',");
            System.out.println("     'Gerente',");
            System.out.println("     '912345678',");
            System.out.println("     1, -- AJUSTE: empresa_id real");
            System.out.println("     NOW()");
            System.out.println("   );");
        }
    }
}