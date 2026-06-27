package com.example.laboratorio2.service.impl;

import com.example.laboratorio2.domain.entity.Pirate;
import com.example.laboratorio2.repository.PirateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PirateServiceImplTest {

    @Mock
    private PirateRepository pirateRepository;

    @InjectMocks
    private PirateServiceImpl pirateService;

    private Pirate pirate;
    private UUID pirateId;

    @BeforeEach
    void setUp() {
        pirateId = UUID.randomUUID();
        pirate = Pirate.builder()
                .id(pirateId)
                .name("Monkey D. Luffy")
                .bounty(3_000_000_000.0)
                .crew("Straw Hat Pirates")
                .isAlive(true)
                .build();
    }

    @Test
    void createPirate_deberiaGuardarYRetornarElPirataCreado() {
        when(pirateRepository.save(pirate)).thenReturn(pirate);

        Pirate result = pirateService.createPirate(pirate);

        assertThat(result.getName()).isEqualTo("Monkey D. Luffy");
        verify(pirateRepository, times(1)).save(pirate);
    }

    @Test
    void getAllPirates_deberiaRetornarListaDePiratasDelRepositorio() {
        Pirate segundoPirata = Pirate.builder()
                .id(UUID.randomUUID())
                .name("Roronoa Zoro")
                .bounty(1_111_000_000.0)
                .crew("Straw Hat Pirates")
                .isAlive(true)
                .build();

        when(pirateRepository.findAll()).thenReturn(List.of(pirate, segundoPirata));

        List<Pirate> result = pirateService.getAllPirates();

        assertThat(result).hasSize(2);
        verify(pirateRepository, times(1)).findAll();
    }

    @Test
    void getPirateById_cuandoExiste_deberiaRetornarElPirata() {
        when(pirateRepository.findById(pirateId)).thenReturn(Optional.of(pirate));

        Pirate result = pirateService.getPirateById(pirateId);

        assertThat(result.getId()).isEqualTo(pirateId);
        verify(pirateRepository).findById(pirateId);
    }

    @Test
    void getPirateById_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(pirateRepository.findById(pirateId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pirateService.getPirateById(pirateId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Pirate not found");

        verify(pirateRepository).findById(pirateId);
        verify(pirateRepository, never()).save(any());
    }

    @Test
    void updatePirate_cuandoExiste_deberiaActualizarLosCampos() {
        Pirate cambios = Pirate.builder()
                .name("Luffy Gear 5")
                .bounty(5_000_000_000.0)
                .crew("Straw Hat Pirates")
                .isAlive(true)
                .build();

        when(pirateRepository.findById(pirateId)).thenReturn(Optional.of(pirate));
        when(pirateRepository.save(any(Pirate.class))).thenAnswer(inv -> inv.getArgument(0));

        Pirate result = pirateService.updatePirate(pirateId, cambios);

        assertThat(result.getName()).isEqualTo("Luffy Gear 5");
        assertThat(result.getBounty()).isEqualTo(5_000_000_000.0);
        verify(pirateRepository).save(pirate);
    }

    @Test
    void updatePirate_cuandoNoExiste_deberiaLanzarExcepcionYNoGuardar() {
        when(pirateRepository.findById(pirateId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pirateService.updatePirate(pirateId, pirate))
                .isInstanceOf(RuntimeException.class);

        verify(pirateRepository, never()).save(any());
    }

    @Test
    void deletePirate_cuandoExiste_deberiaInvocarDeleteById() {
        when(pirateRepository.findById(pirateId)).thenReturn(Optional.of(pirate));

        pirateService.deletePirate(pirateId);

        verify(pirateRepository).deleteById(pirateId);
    }

    @Test
    void deletePirate_cuandoNoExiste_deberiaLanzarExcepcionYNoEliminar() {
        when(pirateRepository.findById(pirateId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pirateService.deletePirate(pirateId))
                .isInstanceOf(RuntimeException.class);

        verify(pirateRepository, never()).deleteById(any());
    }
}