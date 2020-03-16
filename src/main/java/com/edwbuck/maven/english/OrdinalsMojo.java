/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edwbuck.maven.english;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 *
 * <?xml version="1.0" encoding="UTF-8"?>
 * <testsuites disabled="" errors="" failures="" name="" tests="" time="">
 * <testsuite disabled="" errors="" failures="" hostname="" id=""
 * name="" package="" skipped="" tests="" time="" timestamp="">
 * <properties>
 * <property name="" value=""/>
 * </properties>
 * <testcase assertions="" classname="" name="" status="" time="">
 * <skipped/>
 * <error message="" type=""/>
 * <failure message="" type=""/>
 * <system-out/>
 * <system-err/>
 * </testcase>
 * <system-out/>
 * <system-err/>
 * </testsuite>
 * </testsuites> @author edwbuck
 */
@Mojo(name = "ordinals")
public class OrdinalsMojo extends AbstractMojo {

    /**
     * Skip report.
     *
     */
    @Parameter(defaultValue = "false")
    private boolean skip;

    /**
     * The directory containing markdown files of the website being tested.
     *
     * @TODO support generated inputs at some future time.
     */
    @Parameter(required = true)
    protected File sourceDirectory;

    @Parameter(defaultValue = "${project.build.directory}")
    protected File outputDirectory;

    @Parameter(defaultValue = "ordinal-check.xml")
    protected String outputName;

    @Parameter(defaultValue = "false)")
    protected boolean warnOnly;

    /**
     * Fail the scan should no input exist.
     *
     * Set this to false to pass without input. Defaults to true.
     */
    @Parameter(defaultValue = "true")
    private boolean failIfNoInput;

    public void execute() throws MojoExecutionException {
        if (skip) {
            getLog().info("english.ordinals.skip = true: Skipping ordinal checks.");
            return;
        }

        if (getLog().isDebugEnabled()) {
            getLog().debug("Executing English ordinals mojo.");
        }

        List<File> inputFiles = null;
        try {
            getLog().debug("Scanning input files");
            inputFiles = new DirectoryScanner().scan(sourceDirectory);
        } catch (IOException ex) {
            throw new MojoExecutionException("Could not scan " + sourceDirectory, ex);
        }

        // TODO: suppress no tests error at runtime via -D define flag.
        if (inputFiles.isEmpty() && failIfNoInput) {
            throw new MojoExecutionException("No files were checked! (set something TODO) to suppress this at runtime");
        }

        if (getLog().isDebugEnabled()) {
            getLog().debug("sourceDirectory is " + sourceDirectory);
        }

        List<Ordinal> ordinals = new ArrayList<>();
        FileParser parser = new FileParser();
        for (File file : inputFiles) {
            getLog().debug("parsing: " + file);
            ordinals.addAll(parser.parse(file));
        }

        try {
            File outputFile = new File(outputDirectory.getCanonicalFile(), outputName);
            getLog().debug("writing results to " + outputFile.toString());
            new Report().writeResults(outputFile, ordinals);
        } catch (IOException ex) {
            throw new MojoExecutionException("Could not write output file", ex);
        }

        int passed = 0;
        int failed = 0;
        for (Ordinal ordinal : ordinals) {
            if (ordinal.passes()) {
                passed++;
            } else {
                failed++;
                getLog().warn(String.format("Ordinal %s%s in file %s, line %d should be fixed to %1$s%s or %1$s%s",
                        ordinal.getNumber(), ordinal.getSuffix(),
                        ordinal.getFile(),
                        ordinal.getLine(),
                        OrdinalEnding.getEnding(ordinal.getNumber()).singular(),
                        OrdinalEnding.getEnding(ordinal.getNumber()).plural()));
            }
        }
        getLog().info(String.format("ordinals = %d, passed = %d, failed = %d", passed + failed, passed, failed));

        if (!warnOnly && failed > 0) {
            throw new MojoExecutionException("All ordinals did not pass");
        }

    }

}
