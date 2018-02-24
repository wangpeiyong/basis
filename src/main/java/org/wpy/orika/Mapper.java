package org.wpy.orika;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.util.Date;

/**
 * Created by wpy on 2017/6/13.
 */
public class Mapper {
    public static void main(String[] args) {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(Person.class, PersonDto.class)
                .field("name.first", "firstName")
                .field("name.last", "lastName")
                .field("knownAliases{first}", "aliases{[0]}")
                .field("knownAliases{last}", "aliases{[1]}")
                .byDefault()
                .register();
        BoundMapperFacade<PersonDto, Person> mapperFacade = mapperFactory.getMapperFacade(PersonDto.class, Person.class);

        PersonDto personDTO = new PersonDto("wang", "py", new Date(), new String[][]{{"1", "2"}, {"4", "5"}});
        Person map = mapperFacade.map(personDTO);
//        System.out.println(map.getName());
        System.out.println(map);
    }

    protected void configure(MapperFactory factory) {
        factory.classMap(Person.class, PersonDto.class)
                .field("name.first", "firstName")
                .field("name.last", "lastName")
                .field("knownAliases{first}", "aliases{[0]}")
                .field("knownAliases{last}", "aliases{[1]}")
                .byDefault()
                .register();
    }
}