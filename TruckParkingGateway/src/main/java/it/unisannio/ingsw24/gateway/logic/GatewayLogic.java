package it.unisannio.ingsw24.gateway.logic;

import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.Trucker.DTO.TruckerLogin;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;

import java.io.IOException;

public interface GatewayLogic {

    Owner createOwner(Owner owner) throws IOException;

    Trucker createTrucker(Trucker trucker) throws IOException;
    Boolean deleteTruckerByEmail(String email);

}
