package com.example.tasklist

class test {
    fun productExceptSelf(nums: IntArray): IntArray {

        val size: Int = nums.size
        val ans = IntArray(size-1) {
           1
        }

        var aux: Int = 1
        for (i in 0..<size) {
            ans[i] = aux
            aux = nums[i]
        }

        aux = 1
        var i: Int = size-1
        while(i >= 0){
            ans[i] *= aux
            aux *= nums[i]
            i--
        }
        return ans
    }
}