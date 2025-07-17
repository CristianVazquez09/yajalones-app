package com.wolfpack.controller;

import com.wolfpack.dto.ChoferDTO;
import com.wolfpack.model.Chofer;
import com.wolfpack.service.IChoferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/choferes")
@RequiredArgsConstructor
public class ChoferController {

    //@Autowired
    private final IChoferService service;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<ChoferDTO>> buscarTodos() throws Exception{
        List<ChoferDTO> list = service.findAll().stream().map(this::convertToDto).toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChoferDTO> buscarPorId(@PathVariable("id") Integer id) throws Exception {
        Chofer obj = service.findById(id);

        return ResponseEntity.ok(convertToDto(obj));
    }



    @PostMapping
    public ResponseEntity<Void> guardar(@Valid @RequestBody ChoferDTO dto) throws Exception{
        Chofer obj = service.save(convertToEntity(dto));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdChofer()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChoferDTO> actualizar(@Valid @PathVariable("id") Integer id, @RequestBody ChoferDTO dto) throws Exception{
        dto.setIdChofer(id);
        Chofer obj = service.update(id, convertToEntity(dto));

        return ResponseEntity.ok(convertToDto(obj));
    }

    private ChoferDTO convertToDto(Chofer obj){
        return modelMapper.map(obj, ChoferDTO.class);
    }

    private Chofer convertToEntity(ChoferDTO dto){
        return modelMapper.map(dto, Chofer.class);
    }


}
