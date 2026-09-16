package com.sdover.hotelapi.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sdover.hotelapi.service.ReservaService;

@Component
public class ReservaScheduler {

    private final ReservaService reservaService;

    public ReservaScheduler(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @Scheduled(cron = "0 5 15 * * *")
    public void ejecutarFinalizacionReservas() {

        reservaService.finalizarReservasPorFechaSalida();
    }
}
