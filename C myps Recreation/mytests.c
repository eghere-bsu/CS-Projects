#include <stdio.h>
#include <unistd.h>
#include <signal.h>
#include <string.h>
#include <stdlib.h>

#include "ProcEntry.h"

int testCreateDestroy(void) {
   char testName[] = "Create/Destroy Test";
   ProcEntry * testEntry = CreateProcEntry();
   if (testEntry == NULL) {
      DestroyProcEntry(testEntry);
      fprintf(stderr, "%s failed\n", testName);
      return 1;
   }
   DestroyProcEntry(testEntry);
   fprintf(stderr, "%s passed\n", testName);
   return 0;
}

int CreateFromFileDestroy(const char fileName[]) {
   char testName[] = "CreateFromFile/Destroy Test";
   ProcEntry * testEntry = CreateProcEntryFromFile(fileName);
   if (testEntry == NULL) {
      DestroyProcEntry(testEntry);
      fprintf(stderr, "%s failed\n", testName);
      return 1;
   }
   DestroyProcEntry(testEntry);
   fprintf(stderr, "%s passed\n", testName);
   return 0;
}

int CreateFromFilePrintDestroy(const char fileName[]) {
   char testName[] = "CreateFromFile/Print/Destroy Test";
   ProcEntry * testEntry = CreateProcEntryFromFile(fileName);
   if (testEntry == NULL) {
      fprintf(stderr, "%s failed\n", testName);
      DestroyProcEntry(testEntry);
      return 1;
   }
   PrintProcEntry(testEntry);
   DestroyProcEntry(testEntry);
   fprintf(stderr, "%s passed\n", testName);
   return 0;
}

int CreateFromFileNULL(void) {
   char testName[] = "CreateFromFile NULL Test";
   ProcEntry * testEntry = CreateProcEntryFromFile(NULL);
   if (testEntry != NULL) {
      DestroyProcEntry(testEntry);
      fprintf(stderr, "%s failed\n", testName);
      return 1;
   }
   DestroyProcEntry(testEntry);
   fprintf(stderr, "%s passed\n", testName);
   return 0;
}

int CreateProcEntryFromFileTest(void) {
   char testName[] = "CreateProcEntryFromFile: ";
   char fileName[] = "/home/student";
   ProcEntry * testEntry = CreateProcEntryFromFile(fileName);
   if (testEntry != NULL) {
      DestroyProcEntry(testEntry);
      fprintf(stderr, "%s failed\n", testName);
      return 1;
   }
   DestroyProcEntry(testEntry);
   fprintf(stderr, "%s No such file or directory\n", testName);
   return 0;
}

int CreateFromFileDoesNotExist(void) {
   char testName[] = "CreateFromFile DoesNotExist Test";
   char notFile[] = "notRealFile";
   ProcEntry * testEntry = CreateProcEntryFromFile(notFile);
   if (testEntry != NULL) {
      DestroyProcEntry(testEntry);
      fprintf(stderr, "%s failed\n", testName);
      return 1;
   }
   DestroyProcEntry(testEntry);
   fprintf(stderr, "%s passed\n", testName);
   return 0;
}

int CreateFromFileInvalidFormat(void) {
   char testName[] = "CreateFromFile InvalidFormat Test";
   char invalidFormat[] = "432/stat";
   ProcEntry * testEntry = CreateProcEntryFromFile(invalidFormat);
   if (testEntry != NULL) {
      DestroyProcEntry(testEntry);
      fprintf(stderr, "%s failed\n", testName);
      return 1;
   }
   DestroyProcEntry(testEntry);
   fprintf(stderr, "%s passed\n", testName);
   return 0;
}

int main(void) {
   /* TO DO */
   char fileName[] = "test_data/onyx_proc/6/stat";
   testCreateDestroy();
   CreateFromFileDestroy(fileName);
   CreateFromFilePrintDestroy(fileName);
   CreateFromFileNULL();
   CreateProcEntryFromFileTest(); 
   CreateFromFileDoesNotExist();
   CreateFromFileInvalidFormat();
   
   return 0;
}