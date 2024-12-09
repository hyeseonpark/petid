package com.android.data.dto.request

import com.android.domain.entity.PetAppearanceRequestEntity
import com.android.domain.entity.PetImageRequestEntity
import com.android.domain.entity.PetProposerRequestEntity
import com.android.domain.entity.PetRequestEntity

/**
 *
 * @param petRegNo 펫 등록번호
 * @param petName 펫 이름
 * @param ownerId 보호자 ID
 * @param petBirthDate 펫 생년월일
 * @param petSex 펫 성별 (M/W)
 * @param petNeuteredYn 펫 중성화 여부 (Y/N)
 * @param petNeuteredDate 펫 중성화 일시 (펫 중성화 여부가 N일 시, null)
 * @param petAddr 펫 주소
 * @param chipType 칩 타입 (NA / INTERNAL / EXTERNAL)
 * @param appearance 펫 외형
 * @param petImages 펫 사진
 * @param proposer 신청자 정보
 * @param sign 사인 이미지 경로
 */
data class PetRequest(
    val petName: String?,
    val petBirthDate: String?,
    val petSex: Char?,
    val petNeuteredYn: Char?,
    val petNeuteredDate: String?,
    val chipType: String?,
    val appearance: PetAppearanceRequest?,
    val petImages: List<PetImageRequest>?,
    val proposer: PetProposerRequest?,
    val sign: String?,
) {
    class Builder {
        private var petName: String? = null
        private var petBirthDate: String? = null
        private var petSex: Char? = null
        private var petNeuteredYn: Char? = null
        private var petNeuteredDate: String? = null
        private var chipType: String? = null
        private var appearance: PetAppearanceRequest? = null
        private var petImages: List<PetImageRequest>? = null
        private var proposer: PetProposerRequest? = null
        private var sign: String? = null

        /* PetIdStartFragment */
        fun setChipType(chipType: String) = apply { this.chipType = chipType }

        /* UserInfoInputFragment */
        fun setProposer(name: String, address: String, addrressDetails: String,
                        rra: String, rraDetails: String, phone: String) {
            this.proposer = PetProposerRequest(name, address, addrressDetails, rra, rraDetails, phone)
        }

        /* PetInfoInputFragment */
        fun setPetInfo(petName: String, petBirthDate: String, petSex: Char,
            petNeuteredYn: Char, petNeuteredDate: String?) = apply {
                this.petName = petName
                this.petBirthDate = petBirthDate
                this.petSex = petSex
                this.petNeuteredYn = petNeuteredYn
                this.petNeuteredDate = petNeuteredDate
            }

        /* PetPhotoFragment */
        fun setPetImage(petImage: String) = apply {
            this.petImages = listOf(PetImageRequest(petImage))
        }

        /* ScannedInfoFragment */
        fun setAppearance(breed: String, hairColor: String, weight: Int, hairLength: String) = apply {
            this.appearance = PetAppearanceRequest(breed, hairColor, weight, hairLength)
        }

        /* ScannedInfoFragment */
        fun getAppearance() : PetAppearanceRequest? {
            return appearance
        }

        /* SignatureFragment */
        fun setSign(sign: String) = apply { this.sign = sign }

        fun getPetImageName() : String = this.petImages!!.first().imagePath
        fun getSignImageName() : String = this.sign!!

        fun build(): PetRequest {
            return PetRequest(petName, petBirthDate, petSex, petNeuteredYn,
                petNeuteredDate, chipType, appearance, petImages, proposer, sign)
        }
    }
}

fun PetRequest.toDomain() = PetRequestEntity(
    petName = petName!!,
    petBirthDate = petBirthDate!!,
    petSex = petSex!!,
    petNeuteredYn = petNeuteredYn!!,
    petNeuteredDate = petNeuteredDate,
    chipType = chipType!!,
    appearance = appearance!!.toDomain(),
    petImages = petImages!!.toDomain(),
    proposer = proposer!!.toDomain(),
    sign = sign!!,
)