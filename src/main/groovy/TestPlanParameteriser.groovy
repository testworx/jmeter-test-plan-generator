Node test_recording = new XmlParser().parse('../resources/Recording.Template.jmx')
Node test_template = new XmlParser().parse('../resources/TestPlan.Template.jmx')
Node test_plan = test_template

/**
 * Step 1.
 * Parameterise the required session keys
 */
//replace the unique csrfToken value with parameterised value
test_recording.'**'.findAll { elementProp ->
    elementProp.@name == 'csrfToken' }.each {

    it.'**'.findAll { stringProp ->
        stringProp.@name == 'Argument.value' }.each {

        it.children()[0] = '${CSRF_TOKEN}'
    }
}

//replace the unique r value with parameterised value
test_recording.'**'.findAll { elementProp ->
    elementProp.@name == 'r' }.each {

    it.'**'.findAll { stringProp ->
        stringProp.@name == 'Argument.value' }.each {
        it.children()[0] = '${R_TOKEN}'
    }
}

/**
 * Step 2.
 * Pull out the required transactions ready to write into a new test plan
 */
//find the recording controller node that we recorded into and return the parent node
Node test_plan_recorded_transactions = test_recording.hashTree.hashTree.'**'.find { node ->
    node.name() == 'RecordingController'
}.parent()

//find the hashTree element that actually stores the recorded transactions for the recording controller and return the Transaction Controller elements and associated hashTree elements
NodeList test_plan_transactions = test_plan_recorded_transactions.'**'.find { node ->
     node.name() == 'hashTree'
 }.children()[1].children()

/**
 * Step 3.
 * Append the transactions into test_plan (based on a default test plan template containing the relevant regEx extractors for the parameters we have substituted above).
 */
test_plan_transactions.each { it ->
    test_plan.hashTree.hashTree.'**'.find { node ->
        node.name() == 'RegexExtractor'
    }.parent().append(it)
}

/**
 * Step 4.
 * Write the new test plan to a new .jmx file, ready to run.
 */
//print out a new copy of the file contents with parameterised values
File testPlan = new File('../../test/jmeter/TestPlan.Parameterised.jmx')

def printer = new XmlNodePrinter(new PrintWriter(new FileWriter(testPlan)))
printer.preserveWhitespace = true
printer.print(test_plan)
