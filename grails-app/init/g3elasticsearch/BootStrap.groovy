package g3elasticsearch

import com.tucanoo.searchtest.CaseFile
import com.tucanoo.searchtest.InsuranceCase
import com.tucanoo.searchtest.MedicalCase

class BootStrap {
    def elasticSearchService, elasticSearchAdminService

    def init = { servletContext ->
      addTestData()
    }

    def destroy = {
    }
  
  void addTestData() {
    if (! CaseFile.count()) {
      InsuranceCase caseFile = new InsuranceCase()
      caseFile.caseId = 'IC999'
      caseFile.caseName = 'Test Insurance Case'
      caseFile.contactName = 'Tyler Durden'
      caseFile.policyHolder = 'Robert Paulson'
      caseFile.save(failOnError:true)

      MedicalCase medicalCase = new MedicalCase()
      medicalCase.caseId = 'MD9999'
      medicalCase.caseName = 'Medical Case'
      medicalCase.patientName = 'Mary'
      medicalCase.save(failOnError:true, flush:true)

      elasticSearchService.index()
      elasticSearchAdminService.refresh()

      Thread.sleep(1000)
      log.debug("Added Test Data")
    }
  }
}
