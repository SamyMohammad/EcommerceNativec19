package com.example.ecommercenativec19.feature.account

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AccountViewModelTest {
    @Test fun editingLifecycleUpdatesOnlyTheEditedField() {
        val vm = AccountViewModel()
        vm.onStartEditing(AccountFieldType.Mobile)
        assertEquals(AccountFieldType.Mobile, vm.state.value.editing)
        vm.onValueChange(AccountFieldType.Mobile, "0100")
        vm.onFinishEditing()
        assertNull(vm.state.value.editing)
        val byType = vm.state.value.fields.associateBy { it.type }
        assertEquals("0100", byType.getValue(AccountFieldType.Mobile).value)
        assertEquals("Mohamed Mohamed Nabil", byType.getValue(AccountFieldType.FullName).value)
    }
}
