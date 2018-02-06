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
  
  boolean addedTestData = false

  void "Test elastic search returns results for searches against fields in both base and subclasses"() {
    given:
      // add sample data
      addTestData()

      // reindex
      elasticSearchService.index()

      // first search against field in base class
      def baseSearchResult = elasticSearchService.search('IC999')
      println baseSearchResult

      // secondly search against data we know is in MedicalCase subclass
      def subclassSearch1 = elasticSearchService.search('Mary')
      println subclassSearch1

    expect:
      2 == CaseFile.count()
      1 == MedicalCase.count()
      1 == InsuranceCase.count()

      1 == baseSearchResult?.total
      1 == subclassSearch1?.total
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
