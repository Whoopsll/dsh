import { http } from '@/utils/https.js'

const BOOK_API = '/book'

export const exchange = (exchangeDTO) => {
  return http({
    method: 'POST',
    url: BOOK_API + '/exchange',
    data: exchangeDTO
  })
}

export const check = (checkDto) => {
  return http({
    method: 'POST',
    url: BOOK_API + '/check',
    data: checkDto
  })
}

export const resource = (codeDTO) => {
  return http({
    method: 'POST',
    url: BOOK_API + '/resource',
    data: codeDTO
  })
}

export const generateCardNumber = (id) => {
  return http({
    method: 'POST',
    url: BOOK_API + '/generateCardNum/' + id,
  })
}
