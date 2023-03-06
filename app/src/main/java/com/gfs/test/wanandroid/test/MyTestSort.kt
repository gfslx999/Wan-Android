package com.gfs.test.wanandroid.test

import com.gfs.helper.common.expand.printAllItems
import com.gfs.test.wanandroid.mvvm.model.TestSortModel

object MyTestSort {

    /**
     * 根据多个条件执行排序逻辑
     */
    fun sortByMultiRules() {
        val preSortModels = mutableListOf<TestSortModel>()

        preSortModels.add(TestSortModel("by age 009", TestSortModel.SEQUENCE_TYPE_AGE, 9))
        preSortModels.add(TestSortModel("by max", TestSortModel.SEQUENCE_TYPE_MAX, -1))
        preSortModels.add(TestSortModel("by age 007", TestSortModel.SEQUENCE_TYPE_AGE, 7))
        preSortModels.add(TestSortModel("by min", TestSortModel.SEQUENCE_TYPE_MIN, -1))
        preSortModels.add(TestSortModel("by age 011", TestSortModel.SEQUENCE_TYPE_AGE, 11))

        preSortModels.printAllItems("beforeSort")
        // 该方法会按照字段的先后作为优先级，即优先以 sequenceType 为准，若 sequenceType 相等，则再根据 age 排序
        val sortedList = preSortModels.sortedWith(compareBy({ it.sequenceType }, { it.age }))
        sortedList.printAllItems("sorted")
    }

}