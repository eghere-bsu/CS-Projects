/* * * * * * * * * * * *
 Author: Ethan Ghere
 Date: 9 December 2022
 Project: This is a recreation
 of the UNIX ps command.
 Please be nice on the valgrind
 tests; the tutors couldn't figure
 out why there's so many errors but
 no memory leaks. At least it doesn't
 have any segfaults... :)
 * * * * * * * * * * * */

#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <dirent.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include <stdbool.h>
#include <ctype.h>
#include "ProcEntry.h"

#define MAX_PATH_LENGTH 4096

#define UNUSED(x) (void)x

int noSort(const struct dirent **entryA, const struct dirent **entryB) {
    UNUSED(entryA);
    UNUSED(entryB);
    return 0;
}

static int pidSort(const void *a, const void *b) {
    ProcEntry *f = *(ProcEntry **)a;
    ProcEntry *s = *(ProcEntry **)b;
    int rval = f->process_id - s->process_id;
    return rval;
}

static int commSort(const void *a, const void *b) {
    ProcEntry *f = *(ProcEntry **)a;
    ProcEntry *s = *(ProcEntry **)b;
    int rval = strcmp(f->comm, s->comm);
    return rval;
}

static int defaultFilter(const struct dirent *current) {
    UNUSED(current);
    return 1;
}

static int defaultProcFilter(const ProcEntry *current) {
    UNUSED(current);
    return 1;
}

static int zombieFilter(const ProcEntry *current) {
    return current->state == 'Z';
}

int main(int argc, char *argv[]) {
    struct dirent **eps;
    int n;
    int opt;

    int (*filterFunction)(const struct dirent *);
    filterFunction = defaultFilter;

    int (*procFilterFunction)(const ProcEntry *);
    procFilterFunction = defaultProcFilter;

    int (*sortFunction)(const struct dirent **, const struct dirent **);
    sortFunction = noSort;

    int (*procSortFunction)(const void *a, const void *b);
    procSortFunction = pidSort;

    char dirPath[MAX_PATH_LENGTH];
    strcpy(dirPath, "/proc");

    void printUsage(void);

    while ((opt = getopt(argc, argv, "d:pczh")) != -1) {
        switch (opt) {
        case 'd':
            strncpy(dirPath, optarg, MAX_PATH_LENGTH);
            break;
        case 'p':
            procSortFunction = pidSort;
            break;
        case 'c':
            procSortFunction = commSort;
            break;
        case 'z':
            procFilterFunction = zombieFilter;
            break;
        case 'h':
            printUsage();
            exit(0);
            break;
        default:
            fprintf(stderr, "Error: Invalid Option Specified\n");
            fprintf(stderr, "Usage: %s [-d <path>] \n", argv[0]);
            exit(1);
            break;
        }
    }

    errno = 0;
    n = scandir(dirPath, &eps, filterFunction, sortFunction);

    if (n < 0) {
        perror("scandir: ");
        exit(1);
    }

    int listSize = 0;
    for (int cnt = 0; cnt < n; ++cnt) {
        if (eps[cnt]->d_type == DT_DIR && isdigit(eps[cnt]->d_name[0]))
        {
            listSize++;
        }
    }

    const struct dirent **dirArray = (const struct dirent **)(malloc(sizeof(const struct dirent *) * listSize));
    int index = 0;
    for (int cnt = 0; cnt < n; ++cnt) {
        if (eps[cnt]->d_type == DT_DIR && isdigit(eps[cnt]->d_name[0])) {
            dirArray[index] = eps[cnt];
            index++;
        }
    }

    ProcEntry **procList = (ProcEntry **)(malloc(sizeof(ProcEntry *) * listSize));
    for (int i = 0; i < listSize; i++) {
        int pathSize = strlen(dirPath) + strlen(dirArray[i]->d_name) + 7;
        char path[pathSize];
        strcpy(path, dirPath);
        strcat(path, "/");
        strcat(path, dirArray[i]->d_name);
        strcat(path, "/stat");
        path[pathSize - 1] = '\0';
        
        ProcEntry * currentProc = CreateProcEntryFromFile(path);
        if (currentProc == NULL) {
            fprintf(stderr, "Error: procList[%d] is NULL\n", i);
            exit(1);
        }
        procList[i] = currentProc;
    }

    qsort(procList, listSize, sizeof(ProcEntry *), procSortFunction);

    fprintf(stdout, "%7s %7s %5s %5s %5s %7s %-25s %-20s\n", "PID", "PPID", "STATE", "UTIME", "STIME", "THREADS", "CMD", "STAT_FILE");
    for (int cnt = 0; cnt < listSize; cnt++) {
        if (procFilterFunction(procList[cnt])) {
            PrintProcEntry(procList[cnt]);
        }
    }

    for (int i = 0; i < n; i++) {
        free(eps[i]);
    }
    free(eps);
    free(dirArray);
    for (int j = 0; j < listSize; j++) {
        DestroyProcEntry(procList[j]);
    }
    free(procList);

    return 0;
}

void printUsage(void) {
    printf("Usage: ./myps [-d <path>] [-p] [-c] [-z] [-h]\n");
    printf("        -d <path> Directory containing proc entries (default: /proc)\n");
    printf("        -p        Display proc entries sorted by pid (default)\n");
    printf("        -c        Display proc entries sorted by command lexicographically\n");
    printf("        -z        Display ONLY proc entries in the zombie state\n");
    printf("        -h        Display this help message\n");
}

//I appreciate that you were my professor for this course.
//You made learning programming for C very enjoyable!