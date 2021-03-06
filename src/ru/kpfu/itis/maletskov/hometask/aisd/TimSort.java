package ru.kpfu.itis.maletskov.hometask.aisd;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Created by Maletskov on 22.04.2018.
 */
public class TimSort {
   private int size, minrun, count;
   private int[] array;
   private Deque<Integer> stackIndex, stackLen;

   public TimSort(int[] array) {
      this.array = array;
      this.count = 0;
      stackLen = new ArrayDeque<>();
      stackIndex = new ArrayDeque<>();
      this.minrun = this.getMinrun(array.length);
      while (count < array.length) {
         this.getRun();
      }
      while (stackLen.size() > 0) {
         this.canMerge();
      }
      System.out.println(Arrays.toString(array));
   }

   private int getMinrun(int n) {
      int r = 0;
      while (n >= 64) {
         r |= n & 1;
         n >>= 1;
      }
      return n + r;
   }

   public void getRun() {
      int i = count;
      size = 0;
      boolean type = false;
      while (array[i] > array[i + 1]) {
         type = true;
         if (i + 1 > array.length - 2) {
            break;
         }
         i++;
         size++;
      }
      if (i == count) {
         while (array[i] <= array[i + 1]) {
            if (i + 1 > array.length - 2) {
               break;
            }
            i++;
            size++;
         }
      }
      if (type) {
         int k = i;
         for (int j = count; j <= k; j++) {
            int t = array[k];
            array[k] = array[j];
            array[j] = array[k];
         }
      }
      if (size < minrun) {
         i = count + minrun;
         size = i > array.length ? (array.length - count - 1) : minrun;
      }
      this.insertionSort(count, size);
      stackIndex.add(count);
      stackLen.add(size);
      count = i;
   }

   public void insertionSort(int index, int size) {
      for (int i = index + 1; i < index + size; i++) {
         int key = array[i];
         int j = i - 1;
         while (j >= index && array[j] > key) {
            array[j + 1] = array[j];
            j = j - 1;
         }
         array[j + 1] = key;
      }
   }

   public void canMerge() {
      if (stackLen.size() == 0) {
         return;
      }
      if (stackLen.size() > 2) {
         int x = stackIndex.pollFirst();
         int y = stackIndex.pollFirst();
         int z = stackIndex.pollFirst();
         int xlen = stackLen.pollFirst();
         int ylen = stackLen.pollFirst();
         int zlen = stackLen.pollFirst();
         if ((xlen > (ylen + zlen)) && ylen > zlen) {
            merge(x, xlen, y, ylen);
            stackLen.push(zlen);
            stackIndex.push(z);
         } else {
            if (xlen < zlen) {
               merge(x, xlen, y, ylen);
               stackLen.push(zlen);
               stackIndex.push(z);
            } else {
               merge(y, ylen, z, zlen);
               stackLen.push(xlen);
               stackIndex.push(x);
            }
         }
      } else {
         if (stackLen.size() > 1) {
            merge(stackIndex.pollFirst(), stackLen.pollFirst(), stackIndex.pollFirst(), stackLen.pollFirst());
         } else {
            int x = stackIndex.pollFirst();
            System.arraycopy(array, x, array, x, stackLen.pollFirst());
         }
      }
   }

   public void merge(int index, int length, int index1, int length1) {
      int[] x = Arrays.copyOfRange(array, index, index + length);
      int number = 0;
      int number1 = 0;
      int ind = index;
      int b = 0;
      int c = index1;
      for (int i = 0; i < length + length1; i++) {
         if (b < x.length && c < index1 + length1) {
            if (x[b] > array[c]) {
               array[ind++] = array[c++];
               number++;
            } else {
               array[ind++] = x[b++];
               number1++;
            }
         } else {
            array[ind++] = c < index1 + length1 ? array[c++] : x[b++];
         }
         if (number - number1 > 6) {
            int z = binarysearch(c, index1 - c, x[b]);
            System.arraycopy(array, c, array, ind, z - c);
            number = 0;
            number1 = 0;
            i += (z - c);
            ind += (z - c);
            c = z;
         } else {
            if (number1 - number > 6) {
               int z = binarysearch(b + index, length - b, array[c]);
               System.arraycopy(x, b, array, ind, z - index - b);
               number = 0;
               number1 = 0;
               i += z - index - b;
               ind += z - index - b;
               b += z - index - b;
            }
         }
      }
      stackLen.push(length1 + length);
      stackIndex.push(index);
   }

   public int binarysearch(int first, int length, int max) {
      if (array[first] == max || length < 2) {
         return first;
      }
      int middle = first + length / 2;
      if (array[middle] < max) {
         return binarysearch(middle - 1, length / 2, max);
      } else {
         return binarysearch(first, length / 2, max);
      }
   }

   public static void main(String[] args) {
      int[] z = new int[30];
      for (int i = 0; i < z.length; i++) {
         z[i] = (int) (Math.random() * 10000);
      }
      TimSort tms = new TimSort(z);
   }
}