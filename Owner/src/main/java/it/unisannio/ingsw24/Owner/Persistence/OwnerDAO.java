package it.unisannio.ingsw24.Owner.Persistence;

import it.unisannio.ingsw24.Entities.Owner.Owner;
import it.unisannio.ingsw24.Entities.Trucker.Trucker;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerDAO {

    String DATABASE_NAME = "TruckParking";
    String COLLECTION_NAME = "Users";
    String ELEMENT_ID = "id_owner";
    String ELEMENT_NAME = "name";
    String ELEMENT_SURNAME = "surname";
    String ELEMENT_GENDER = "gender";
    String ELEMENT_BDATE = "bDate";
    String ELEMENT_EMAIL = "email";
    String ELEMENT_ROLE = "role";
    String ELEMENT_PASSWORD = "password";
    String ELEMENT_PARKS = "parks";


    Owner createOwner(Owner owner);

}


