package pe.edu.cibertec.cl1_frontend_jossetgutierrez.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.cl1_frontend_jossetgutierrez.dto.PlacaRequestDTO;
import pe.edu.cibertec.cl1_frontend_jossetgutierrez.dto.PlacaResponseDTO;
import pe.edu.cibertec.cl1_frontend_jossetgutierrez.viewModel.PlacaModel;


@Controller
@RequestMapping("/rueditas")
public class PlacaController {
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/inicio")
    public String inicio(Model model) {
        PlacaModel placaModel = new PlacaModel(
                "00",
                "",
                "",
                "",
                "",
                "",
                ""
        );
        model.addAttribute("PlacaModel", placaModel);
        return "inicio";
    }

    @PostMapping("/validar")
    public String validar(@RequestParam("placa") String placa, Model model) {
        if (placa == null || placa.trim().length() != 7) {
            PlacaModel placaModel = new PlacaModel(
                "01",
                "Debe ingresar una placa correcta",
                "",
                "",
                "",
                "",
                ""
            );
            model.addAttribute("PlacaModelo", placaModel);
        }

        try {
            String endpoint = "http://localhost:8081/placa/datos";
            PlacaRequestDTO placaRequestDTO = new PlacaRequestDTO(placa);
            PlacaResponseDTO placaResponseDTO = restTemplate.postForObject(endpoint, placaRequestDTO, PlacaResponseDTO.class);

            if (placaResponseDTO.codigo().equals("00")) {
                PlacaModel placaModel = new PlacaModel(
                    "00",
                    "",
                    placaResponseDTO.marca(),
                    placaResponseDTO.modelo(),
                    placaResponseDTO.numeroAsientos(),
                    placaResponseDTO.precio(),
                    placaResponseDTO.color()
                );
                model.addAttribute("PlacaModel", placaModel);
                return "placa";
            } else {
                PlacaModel placaModel = new PlacaModel(
                    "02",
                    "Placa no encontrada",
                    "",
                    "",
                    "",
                    "",
                    ""
                );
                model.addAttribute("PlacaModel", placaModel);
                return "inicio";
            }
        } catch (RestClientException e) {
            PlacaModel placaModel = new PlacaModel(
                    "99",
                    "No se pudo realizar la búsqueda. Error de Servicio",
                    "",
                    "",
                    "",
                    "",
                    ""
            );
            model.addAttribute("PlacaModel", placaModel);
            System.out.println(e.getMessage());
            return "inicio";
        }
    }
}
