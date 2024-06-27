package it.unisannio.ingsw24.gateway.logic;

import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;

import java.io.IOException;

public interface GatewayLogic {

    Owner createOwner(Owner owner) throws IOException;
    Boolean deleteOwnerByEmail(String email) throws IOException;
    Owner getOwnerByEmail(String email) throws IOException;
    Owner getOwnerById(int id) throws IOException;
    Trucker createTrucker(Trucker trucker) throws IOException;
    Boolean deleteTruckerByEmail(String email);
    //test di
}
