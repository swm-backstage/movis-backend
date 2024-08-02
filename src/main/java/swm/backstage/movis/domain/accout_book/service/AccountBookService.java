package swm.backstage.movis.domain.accout_book.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.backstage.movis.domain.accout_book.AccountBook;
import swm.backstage.movis.domain.accout_book.repository.AccountBookRepository;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountBookService {
    private final AccountBookRepository accountBookRepository;

    @Transactional
    public AccountBook getAccountBookByClubId(String clubId) {
        return accountBookRepository.findByClubIdWithLock(clubId)
                .orElseThrow(()->new BaseException("account book is not found", ErrorCode.ELEMENT_NOT_FOUND));
    }


}
