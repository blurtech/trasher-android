package tech.blur.trasher.presentation.view

interface SupportBackStack {
    fun resetBackStack()
    fun popBackStack(): Boolean
    fun onBackPressed()
}