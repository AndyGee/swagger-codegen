package io.swagger.codegen.swift;

import io.swagger.codegen.CodegenConstants;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.DefaultCodegen;
import io.swagger.codegen.languages.SwiftCodegen;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SwiftCodegenTest {

    SwiftCodegen swiftCodegen = new SwiftCodegen();

    @Test
    public void shouldNotBreakCorrectName() throws Exception {
        Assert.assertEquals(swiftCodegen.toSwiftyEnumName("EntryName"), "EntryName");
    }

    @Test
    public void testSingleWordAllCaps() throws Exception {
        Assert.assertEquals(swiftCodegen.toSwiftyEnumName("VALUE"), "Value");
    }

    @Test
    public void testSingleWordLowercase() throws Exception {
        Assert.assertEquals(swiftCodegen.toSwiftyEnumName("value"), "Value");
    }

    @Test
    public void testCapitalsWithUnderscore() throws Exception {
        Assert.assertEquals(swiftCodegen.toSwiftyEnumName("ENTRY_NAME"), "EntryName");
    }

    @Test
    public void testCapitalsWithDash() throws Exception {
        Assert.assertEquals(swiftCodegen.toSwiftyEnumName("ENTRY-NAME"), "EntryName");
    }

    @Test
    public void testCapitalsWithSpace() throws Exception {
        Assert.assertEquals(swiftCodegen.toSwiftyEnumName("ENTRY NAME"), "EntryName");
    }

    @Test
    public void testLowercaseWithUnderscore() throws Exception {
        Assert.assertEquals(swiftCodegen.toSwiftyEnumName("entry_name"), "EntryName");
    }

    @Test
    public void testSlash() throws Exception {
        Assert.assertEquals(swiftCodegen.toSwiftyEnumName("application/x-tar"), "ApplicationXTar");
    }

    @Test(description = "returns NSData when response format is binary")
    public void binaryDataTest() {
        final Swagger model = new SwaggerParser().read("src/test/resources/2_0/binaryDataTest.json");
        final DefaultCodegen codegen = new SwiftCodegen();
        final String path = "/tests/binaryResponse";
        final Operation p = model.getPaths().get(path).getPost();
        final CodegenOperation op = codegen.fromOperation(path, "post", p, model.getDefinitions());

        Assert.assertEquals(op.returnType, "NSData");
        Assert.assertEquals(op.bodyParam.dataType, "NSData");
        Assert.assertTrue(op.bodyParam.isBinary);
        Assert.assertTrue(op.responses.get(0).isBinary);
    }

    @Test(description = "returns ISOFullDate when response format is date")
    public void dateTest() {
        final Swagger model = new SwaggerParser().read("src/test/resources/2_0/datePropertyTest.json");
        final DefaultCodegen codegen = new SwiftCodegen();
        final String path = "/tests/dateResponse";
        final Operation p = model.getPaths().get(path).getPost();
        final CodegenOperation op = codegen.fromOperation(path, "post", p, model.getDefinitions());

        Assert.assertEquals(op.returnType, "ISOFullDate");
        Assert.assertEquals(op.bodyParam.dataType, "ISOFullDate");
    }

    @Test
    public void testDefaultPodAuthors() throws Exception {
        // Given

        // When
        swiftCodegen.processOpts();

        // Then
        final String podAuthors = (String) swiftCodegen.additionalProperties().get(SwiftCodegen.POD_AUTHORS);
        Assert.assertEquals(podAuthors, SwiftCodegen.DEFAULT_POD_AUTHORS);
    }

    @Test
    public void testPodAuthors() throws Exception {
        // Given
        final String swaggerDevs = "Swagger Devs";
        swiftCodegen.additionalProperties().put(SwiftCodegen.POD_AUTHORS, swaggerDevs);

        // When
        swiftCodegen.processOpts();

        // Then
        final String podAuthors = (String) swiftCodegen.additionalProperties().get(SwiftCodegen.POD_AUTHORS);
        Assert.assertEquals(podAuthors, swaggerDevs);
    }

    @Test
    public void testInitialConfigValues() throws Exception {
        final SwiftCodegen codegen = new SwiftCodegen();
        codegen.processOpts();

        Assert.assertEquals(codegen.additionalProperties().get(CodegenConstants.HIDE_GENERATION_TIMESTAMP), Boolean.TRUE);
        Assert.assertEquals(codegen.isHideGenerationTimestamp(), true);
    }

    @Test
    public void testSettersForConfigValues() throws Exception {
        final SwiftCodegen codegen = new SwiftCodegen();
        codegen.setHideGenerationTimestamp(false);
        codegen.processOpts();

        Assert.assertEquals(codegen.additionalProperties().get(CodegenConstants.HIDE_GENERATION_TIMESTAMP), Boolean.FALSE);
        Assert.assertEquals(codegen.isHideGenerationTimestamp(), false);
    }

    @Test
    public void testAdditionalPropertiesPutForConfigValues() throws Exception {
        final SwiftCodegen codegen = new SwiftCodegen();
        codegen.additionalProperties().put(CodegenConstants.HIDE_GENERATION_TIMESTAMP, false);
        codegen.processOpts();

        Assert.assertEquals(codegen.additionalProperties().get(CodegenConstants.HIDE_GENERATION_TIMESTAMP), Boolean.FALSE);
        Assert.assertEquals(codegen.isHideGenerationTimestamp(), false);
    }

}