package br.com.taxApi.taxcalculator.fixture;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.taxApi.taxcalculator.dto.WorkerAdmDTO;

public class WorkerAdmTemplateLoader implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(WorkerAdmDTO.class).addTemplate("valid-workeradm", new Rule() {{
            add("email", "alessandro@teste.com");
            add("password", "Alessandro123**");
        }});
    }
}
