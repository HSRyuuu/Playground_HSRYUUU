package com.hsryuuu.traffic.fcfs.basic;

import com.hsryuuu.traffic.fcfs.basic.service.TicketingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/ticketing")
@RestController
public class TicketingController {

  private final TicketingService ticketingService;

  @PostMapping("/apply")
  public void applyTicketing(){
    ticketingService.apply();
  }


}
