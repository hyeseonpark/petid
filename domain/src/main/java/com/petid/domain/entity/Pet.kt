package com.petid.domain.entity

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
data class Pet(
    val petName: String,
    val petBirthDate: String,
    val petSex: Char,
    val petNeuteredYn: Char,
    val petNeuteredDate: String?,
    val chipType: String,
    val appearance: PetAppearance,
    val petImages: List<PetImage>,
    val proposer: PetProposer,
    val sign: String,
) {
    class Builder {
        private var petName: String? = null
        private var petBirthDate: String? = null
        private var petSex: Char? = null
        private var petNeuteredYn: Char? = null
        private var petNeuteredDate: String? = null
        private var chipType: String? = null
        private var appearance: PetAppearance? = null
        private var petImages: List<PetImage>? = null
        private var proposer: PetProposer? = null
        private var sign: String? = null

        /* PetIdStartFragment */
        fun setChipType(chipType: String) = apply { this.chipType = chipType }

        /* UserInfoInputFragment */
        fun setProposer(name: String, address: String, addrressDetails: String,
                        rra: String, rraDetails: String, phone: String) {
            this.proposer = PetProposer(name, address, addrressDetails, rra, rraDetails, phone)
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
            this.petImages = listOf(PetImage(petImage))
        }

        /* ScannedInfoFragment */
        fun setAppearance(breed: String, hairColor: String, weight: Double, hairLength: String) = apply {
            this.appearance = PetAppearance(breed, hairColor, weight, hairLength)
        }

        /* ScannedInfoFragment */
        fun getAppearance() : PetAppearance? {
            return appearance
        }

        /* SignatureFragment */
        fun setSign(sign: String) = apply { this.sign = sign }

        fun getPetImageName() : String = this.petImages!!.first().imagePath
        fun getSignImageName() : String = this.sign!!

        /**
         * build는 CheckingInfoFragment, SignatureFragment에서 이루어짐
         * CheckingInfoFragment: 단순 정보 확인을 위함, forGenerting = false
         * SignatureFragment: 실제 펫아이디 생성을 위함, forGenerating = true
         */
        fun build(forGenerating: Boolean): Pet {
            require(!petName.isNullOrEmpty()) { "펫 이름은 필수입니다" }
            require(!petBirthDate.isNullOrEmpty()) { "생년월일은 필수입니다" }
            require(petSex != null) { "성별은 필수입니다" }
            require(petNeuteredYn != null) { "중성화 여부는 필수입니다" }
            require(!chipType.isNullOrEmpty()) { "칩 타입은 필수입니다" }
            require(appearance != null) { "외형 정보는 필수입니다" }
            require(petImages != null) { "펫 이미지는 필수입니다" }
            require(proposer != null) { "신청자 정보는 필수입니다" }
            if (forGenerating) require(sign != null) { "서명은 필수입니다" }

            return Pet(
                petName = petName!!,
                petBirthDate = petBirthDate!!,
                petSex = petSex!!,
                petNeuteredYn = petNeuteredYn!!,
                petNeuteredDate = petNeuteredDate,
                chipType = chipType!!,
                appearance = appearance!!,
                petImages = petImages!!,
                proposer = proposer!!,
                sign = if(forGenerating) sign!! else "",
            )
        }
    }
}