package ifrn.pi.eventos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ifrn.pi.eventos.models.Evento;

@Controller
@RequestMapping("/eventos")
public class EventosController {

    @GetMapping("/form")
    public String form() {
        return "formEvento";
    }

    @PostMapping("/form")
    public String salvar(Evento evento) {
        System.out.println("----- DADOS DO EVENTO RECEBIDO -----");
        System.out.println("Nome do Evento: " + evento.getNome());
        System.out.println("Local Realização: " + evento.getLocal());
        System.out.println("Data: " + evento.getData());
        System.out.println("Horário: " + evento.getHorario());
        System.out.println("------------------------------------");
        
        return "redirect:/eventos/form";
    }
}

