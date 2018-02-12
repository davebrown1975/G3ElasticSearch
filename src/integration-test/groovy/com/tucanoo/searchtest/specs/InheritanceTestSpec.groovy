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

  void "confirm we have entries persisted"() {
    expect:
      // check we've definitely got expected records in the db
      2 == CaseFile.count()
      1 == MedicalCase.count()
      1 == InsuranceCase.count()
  }

  void "Test elastic search returns results for searches against fields in base class"() {
    when:
      // first search against field in base class
      def baseSearchResult = elasticSearchService.search('Test')
      println "SR = "
      println baseSearchResult
    then:
      // check results were returned from ES
      1 == baseSearchResult?.total
      "Test Insurance Case" == baseSearchResult.searchResults[0].caseName    
  }

  void "Test elastic search returns results for searches against fields subclasses"() {
    when:
      // secondly search against data we know is in MedicalCase subclass
      def subclassSearch1 = elasticSearchService.search('Mary')
      println "SS = "
      println subclassSearch1

    then:
      // check results were returned from ES
      1 == subclassSearch1?.total
      "Medical Case" == subclassSearch1.searchResults[0].caseName    // FAILS as it doesn't retrieve the full record
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
