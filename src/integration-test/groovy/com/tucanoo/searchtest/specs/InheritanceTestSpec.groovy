package com.tucanoo.searchtest.specs

import com.tucanoo.searchtest.CaseFile
import com.tucanoo.searchtest.InsuranceCase
import com.tucanoo.searchtest.MedicalCase
import grails.plugins.elasticsearch.ElasticSearchAdminService
import grails.plugins.elasticsearch.ElasticSearchService
import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@Integration
@Rollback
class InheritanceTestSpec extends Specification {

  @Autowired
  ElasticSearchService elasticSearchService
  ElasticSearchAdminService elasticSearchAdminService

  boolean addedTestData = false

  void "Test elastic search returns results for searches against fields in both base and subclasses"() {
    given:
      // add sample data
      addTestData()

      // ES doesn't appear to be mirroring changes so force reindex - doesn't appear to be happening either.
      elasticSearchService.index()
      elasticSearchAdminService.refresh()


    when:
      // first search against field in base class
      def baseSearchResult = elasticSearchService.search('Test')

      // secondly search against data we know is in MedicalCase subclass
      def subclassSearch1 = elasticSearchService.search('Mary')

    then:
      // check we've definately got expected records in the db
      2 == CaseFile.count()
      1 == MedicalCase.count()
      1 == InsuranceCase.count()

      // check results were returned from ES
      1 == baseSearchResult?.total
      1 == subclassSearch1?.total

      // next will test domain instances are correctly/fully returned from ES


  }

  void addTestData() {
    if (! addedTestData) {
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

      addedTestData = true
    }
  }
}
