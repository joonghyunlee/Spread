package org.joonghyunlee.spread.API.web;

import lombok.RequiredArgsConstructor;
import org.joonghyunlee.spread.API.domain.division.DivisionService;
import org.joonghyunlee.spread.API.domain.division.model.Division;
import org.joonghyunlee.spread.API.web.model.CreateDivisionRequest;
import org.joonghyunlee.spread.API.web.model.CreateDivisionResponse;
import org.joonghyunlee.spread.API.web.model.GetDivisionResponse;
import org.joonghyunlee.spread.API.web.model.ReceiveDividendResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DivisionController {

    private final DivisionService divisionService;

    @PostMapping("/v1.0/divisions")
    public CreateDivisionResponse create(@RequestHeader("X-ROOM-ID") String roomId,
                                         @RequestHeader("X-USER-ID") String userId,
                                         @RequestBody CreateDivisionRequest request) {
        Division division = divisionService.create(roomId, userId, request);

        return CreateDivisionResponse.of(division);
    }

    @GetMapping("/v1.0/divisions/{divisionId}")
    public GetDivisionResponse get(@RequestHeader("X-ROOM-ID") String roomId,
                                   @RequestHeader("X-USER-ID") String userId,
                                   @PathVariable("divisionId") String divisionId) {
        Division division = divisionService.get(roomId, userId, divisionId);

        return GetDivisionResponse.of(division);
    }

    @PostMapping("/v1.0/divisions/{divisionId}/receive")
    public ReceiveDividendResponse receive(@RequestHeader("X-ROOM-ID") String roomId,
                                           @RequestHeader("X-USER-ID") String userId,
                                           @PathVariable("divisionId") String divisionId) {
        long share = divisionService.receive(roomId, userId, divisionId);

        return ReceiveDividendResponse.of(share);
    }
}
