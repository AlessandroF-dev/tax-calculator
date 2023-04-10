package br.com.taxApi.taxcalculator.fixture;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.taxApi.taxcalculator.dto.WorkerDTO;

public class WorkerTemplateLoader implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(WorkerDTO.class).addTemplate("valid-worker", new Rule(){{
            add("id", random(1L));
            add("name", "Alessandro");
            add("password", "Alessandro123**");
            add("email", "alessandro@teste.com");
            add("office", "Dev");
            add("salary", 8000.00);
            add("age", 23);
            add("active", true);
        }});
    }
}
