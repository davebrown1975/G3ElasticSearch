package com.tucanoo.searchtest

abstract class CaseFile {

    String caseId
    String caseName
    String contactName

    String anotherField1

    static constraints = {
      caseId nullable: false
      caseName nullable: false
      contactName nullable: true

      anotherField1 nullable: true
    }

    static searchable = {
      root true
      only = ['caseId','caseName','contactName']
    }
}
