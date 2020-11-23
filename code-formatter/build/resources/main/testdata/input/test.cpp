// C++ program for implementation of Bubble sort

#include <bits/stdc++.h>
using namespace std;

void swap(int *xp, int *yp)
{
    int temp = *xp;
    *xp = *yp;
    *yp = temp;
}

// A function to implement bubble sort
void bubbleSort(int arr[], int n)
{
    int i, j;
    for (i = 0; i < n - 1; i++)

        // Last i elements are already in place
        for (j = 0; j < n - i - 1; j++)
            if (arr[j] > arr[j + 1])
                swap(&arr[j], &arr[j + 1]);
}

/* Function to print an array */
void printArray(int arr[], int size)
{
    int i;
    for (i = 0; i < size; i++)
        cout << arr[i] << " ";
    cout << endl;
}

// Driver code
int main()
{
    int arr[] = {64, 34, 25, 12, 22, 11, 90};
    int n = sizeof(arr) / sizeof(arr[0]);
    bubbleSort(arr, n);
    cout << "Sorted array: \n";
    printArray(arr, n);
    return 0;
}

// This code is contributed by rathbhupendra

#ifdef GHGHG
#include <iostream>

// void printArray(int *array, int n)
// {
//     for (int i = 0; i < n; ++i)
//         std::cout << array[i] << std::endl;
// }

// void merge(int *array, int low, int mid, int high)
// {
//     int temp[high + 1];
//     int i = low;
//     int j = mid + 1;
//     int k = 0;

//     while (i <= mid && j <= high)

//     {
//         if (array[i] <= array[j])
//             temp[k++] = array[i++];
//         else
//             temp[k++] = array[j++];
//     }
//     while (i <= mid)
//         temp[k++] = array[i++];
//     while (j <= high)
//         temp[k++] = array[j++];
//     k--;
//     while (k >= 0)
//     {
//         array[k + low] = temp[k];
//         k--;
//     }
// }

// void mergeSort(int *array, int low, int high)
// {
//     int mid;

//     if (low < high)
//     {
//         mid = (low + high) / 2;
//         mergeSort(array, low, mid);
//         mergeSort(array, mid + 1, high);
//         merge(array, low, mid, high);
//     }
// }

// int main()
// {
//     int array[] = {95, 45, 48, 98, 1, 485, 65, 478, 1, 2325};
//     int n = sizeof(array)/sizeof(array[0]);

//     std::cout << "Before Merge Sort :" << std::endl;
//     printArray(array, n);

//     mergeSort(array, 0, n - 1);

//     std::cout << "After Merge Sort :" << std::endl;
//     printArray(array, n);
//     return (0);
// }

// mytype myname(){
//     void foo(){
//         for(int i=0; i<10;i++){}
//     }
// }

// for (int i=0; i<10; i++){}
int i;
for (; int a;) // dikgjdiu
{
    ;
}

while (true)
{
}