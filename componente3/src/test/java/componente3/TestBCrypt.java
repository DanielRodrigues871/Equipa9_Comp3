package componente3;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Arrays;
import java.util.List;

public class TestBCrypt {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashDoBanco = "$2a$10$QrZkEckVusUlYBO.mjZaN.nPeRo33Hoor5I1amkgzwlSR/vYJ.c8K";
        
        // Teste várias senhas possíveis
        List<String> senhasParaTestar = Arrays.asList(
            "Isabel67!",      // Sua tentativa atual
            "Isabel67@",      // @ em vez de !
            "Isabel68!",      // número diferente
            "Isabel67",       // sem caractere especial
            "Isabel@67",      // @ no meio
            "Isabel!67",      // ! no meio
            "isabel67!",      // minúscula
            "Isabel 67!",     // com espaço
            "Password123!",   // comum
            "Admin123!",      // outra comum
            "Teste123@",      // outra comum
            "12345678",       // simples
            "senha123"        // simples
        );
        
        System.out.println("Testando hash: " + hashDoBanco);
        for (String senha : senhasParaTestar) {
            boolean match = encoder.matches(senha, hashDoBanco);
            if (match) {
                System.out.println("✓ ENCONTRADA! Senha: '" + senha + "'");
                return;
            } else {
                System.out.println("✗ '" + senha + "' não corresponde");
            }
        }
        
        System.out.println("\nNenhuma senha testada corresponde ao hash.");
        System.out.println("O hash pode ter sido gerado com uma senha completamente diferente.");
        
        // Mostre alguns exemplos de hashes válidos
        System.out.println("\nExemplos de hashes válidos para 'Isabel67!':");
        for (int i = 0; i < 3; i++) {
            String novoHash = encoder.encode("Isabel67!");
            System.out.println("Hash " + (i+1) + ": " + novoHash);
        }
    }
}
