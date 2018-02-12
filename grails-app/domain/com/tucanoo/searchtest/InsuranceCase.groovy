package com.tucanoo.searchtest

class InsuranceCase extends CaseFile {

    String policyHolder

    // .. many other fields omitted.

    static constraints = {
      policyHolder nullable: false
    }

    static searchable = {
      root true
      only = ['policyHolder']
    }
}
