package spring.study.week3.common.util;

import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import spring.study.week3.common.dto.SeedDto;

import java.util.Random;

@Component
public class UtilServiceImpl implements UtilService {

    @Override
    public Faker getFaker(SeedDto seedDto) {
        return new Faker(new Random(seedDto.getSeed()));
    }

}
