package ar.edu.utn.dds.k3003.antiSpam;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AntiSpamBasico implements AntiSpamService{
    private final List<String> palabrasProhibidas = List.of(
            "spam"
    );

    @Override
    public boolean esSpam(String texto) {
        if (texto == null) return false;

        String textoMin = texto.toLowerCase();
        return palabrasProhibidas.stream()
                .anyMatch(textoMin::contains);
    }
}
