package ifrn.pi.eventos.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ifrn.pi.eventos.models.Convidado;
import ifrn.pi.eventos.models.Evento;
import ifrn.pi.eventos.repositories.ConvidadoRepository;
import ifrn.pi.eventos.repositories.EventoRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/eventos")
public class EventosController {

    @Autowired
    private EventoRepository er;

    @Autowired
    private ConvidadoRepository cr;

    @GetMapping("/form")
    public String form(Evento evento) {
        return "formEvento";
    }

    @PostMapping
    public String salvar(
            @Valid Evento evento,
            BindingResult result,
            RedirectAttributes attributes) {

        if (result.hasErrors()) {
            return "formEvento";
        }

        er.save(evento);

        attributes.addFlashAttribute(
                "mensagem",
                "Evento salvo com sucesso!"
        );

        return "redirect:/eventos";
    }

    @GetMapping
    public ModelAndView listar() {

        List<Evento> eventos = er.findAll();

        ModelAndView mv = new ModelAndView("lista");
        mv.addObject("eventos", eventos);

        return mv;
    }

    @GetMapping("/{id}")
    public ModelAndView detalhesEvento(@PathVariable("id") Long id) {

        ModelAndView mv = new ModelAndView("detalhes");

        Optional<Evento> opt = er.findById(id);

        if (opt.isEmpty()) {
            mv.setViewName("redirect:/eventos");
            return mv;
        }

        Evento evento = opt.get();

        mv.addObject("evento", evento);
        mv.addObject("convidado", new Convidado());

        List<Convidado> convidados = cr.findByEvento(evento);

        mv.addObject("convidados", convidados);

        return mv;
    }

    @PostMapping("/{id}")
    public ModelAndView salvarConvidado(
            @PathVariable("id") Long id,
            @Valid Convidado convidado,
            BindingResult result,
            RedirectAttributes attributes) {

        Optional<Evento> opt = er.findById(id);

        if (opt.isEmpty()) {
            return new ModelAndView("redirect:/eventos");
        }

        Evento evento = opt.get();

        if (result.hasErrors()) {

            ModelAndView mv = new ModelAndView("detalhes");

            mv.addObject("evento", evento);
            mv.addObject("convidado", convidado);
            mv.addObject("convidados", cr.findByEvento(evento));

            return mv;
        }

        convidado.setEvento(evento);

        cr.save(convidado);

        attributes.addFlashAttribute(
                "mensagem",
                "Convidado adicionado com sucesso!"
        );

        return new ModelAndView("redirect:/eventos/" + id);
    }

    @GetMapping("/{id}/selecionar")
    public ModelAndView selecionarEvento(@PathVariable Long id) {

        ModelAndView mv = new ModelAndView();

        Optional<Evento> opt = er.findById(id);

        if (opt.isEmpty()) {
            mv.setViewName("redirect:/eventos");
            return mv;
        }

        Evento evento = opt.get();

        mv.setViewName("formEvento");
        mv.addObject("evento", evento);

        return mv;
    }

    @GetMapping("/{id}/remover")
    public String apagarEvento(@PathVariable Long id) {

        Optional<Evento> opt = er.findById(id);

        if (opt.isPresent()) {

            Evento evento = opt.get();

            List<Convidado> convidados = cr.findByEvento(evento);

            cr.deleteAll(convidados);

            er.delete(evento);
        }

        return "redirect:/eventos";
    }

    @GetMapping("/{idEvento}/convidados/{idConvidado}/remover")
    public String apagarConvidado(
            @PathVariable Long idEvento,
            @PathVariable Long idConvidado,
            RedirectAttributes attributes) {

        Optional<Convidado> opt = cr.findById(idConvidado);

        if (opt.isPresent()) {

            Convidado convidado = opt.get();

            cr.delete(convidado);

            attributes.addFlashAttribute(
                    "mensagem",
                    "Convidado removido com sucesso!"
            );
        }

        return "redirect:/eventos/" + idEvento;
    }
}