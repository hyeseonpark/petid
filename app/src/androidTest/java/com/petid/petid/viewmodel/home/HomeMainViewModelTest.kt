package com.petid.petid.viewmodel.home

import com.petid.domain.entity.MemberInfoEntity
import com.petid.domain.repository.HomeMainRepository
import com.petid.domain.repository.MyInfoRepository
import com.petid.domain.repository.PetInfoRepository
import com.petid.domain.util.ApiResult
import com.petid.petid.ui.state.CommonApiState
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class HomeMainViewModelTest {

    private lateinit var homeMainRepository: HomeMainRepository
    private lateinit var myInfoRepository: MyInfoRepository
    private lateinit var petInfoRepository: PetInfoRepository

    private lateinit var homeMainViewModel: HomeMainViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        homeMainRepository = mockk()
        myInfoRepository = mockk()
        petInfoRepository = mockk()

        homeMainViewModel = HomeMainViewModel(homeMainRepository, myInfoRepository, petInfoRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `멤버정보_가져오기_성공_시_CommonApiState를_Success로_업데이트한다`() = runTest {
        // given
        val emptyEntity : MemberInfoEntity = mockk()
        coEvery { myInfoRepository.getMemberInfo() } returns ApiResult.Success(emptyEntity)

        // when
        homeMainViewModel.getMemberInfo()

        // then
        val result = homeMainViewModel.getMemberInfoResult.first()
        assertEquals(CommonApiState.Success(emptyEntity), result)
    }
}