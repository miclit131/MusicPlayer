package de.hdm_stuttgart.cmpt;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({MediaLibraryTest.class, FileManagerFactoryTest.class,
        LocalFileManagerTest.class, PerformanceTest.class })
public class TestSuite {
}