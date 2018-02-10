package com.tucanoo.searchtest

class MedicalCase extends CaseFile {

    String patientName

    // .. many other fields
    String anotherMedicalField1
    String anotherMedicalField2
    String anotherMedicalField3
    Integer anotherMedicalField4
    Integer anotherMedicalField5

    static constraints = {
      patientName nullable: false
      anotherMedicalField1 nullable: true
      anotherMedicalField2 nullable: true
      anotherMedicalField3 nullable: true
      anotherMedicalField4 nullable: true
      anotherMedicalField5 nullable: true
    }

    static searchable = {
      root true
      only = ['patientName']
    }
}
