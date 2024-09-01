package spring.study.week3.common.util;

import net.datafaker.Faker;
import spring.study.week3.common.dto.SeedDto;

public interface UtilService {
    Faker getFaker(SeedDto seedDto);
}
