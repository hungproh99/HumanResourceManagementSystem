package com.csproject.hrm.repositories.Custom;

import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.repositories.Impl.EmployeeRepositoryCustom;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<HrmResponse> findAllEmployee(int limit, int page) {
        String sql = "select e.employee_id,e.full_name ," +
                "e.company_email as email,e.work_status ,e.phone_number as phone," +
                "(case when e.gender = 1 then \"Male\" when e.gender = 2 then \"Felame\" end) as gender," +
                "e.birth_date,j.position as job,o.name as office,a.name as area,ct.name as contract," +
                "concat(year(curdate())-year(wc.start_date),' year ',month(curdate()-wc.start_date)," +
                "' month ',day(curdate()-wc.start_date), ' day') as seniority,wc.start_date as start_date " +
                "from employee e " +
                "left join working_contract wc on e.employee_id = wc.employee_id " +
                "left join contract_type ct on ct.type_id = wc.type_id " +
                "left join working_place wp on wp.working_contract_id = wc.working_contract_id " +
                "left join area a on a.area_id = wp.area_id " +
                "left join office o on o.office_id = wp.office_id " +
                "left join job j on j.job_id = wp.job_id limit :offset,:limit";
        TypedQuery<HrmResponse> query = entityManager.createNativeQuery(sql)
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(Transformers.aliasToBean(HrmResponse.class));
        query.setParameter("offset", (page - 1) * limit);
        query.setParameter("limit", limit);

        return query.getResultList();
    }
}
