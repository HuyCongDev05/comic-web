import Pagination from '@mui/material/Pagination';
import PaginationItem from '@mui/material/PaginationItem';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';

export default function CustomPagination({ count, page, onChange }) {
  return (
    <Pagination
      count={count}
      page={page}
      onChange={onChange}
      variant="outlined"
      sx={{
        display: 'flex',
        justifyContent: 'center',
        mt: 2,
        '& .MuiPaginationItem-root': {
          color: '#fff',
          padding: '10px'
        },
        '& .MuiPaginationItem-root.Mui-selected': {
          backgroundColor: '#d46fff',
          color: '#fff',
          borderColor: '#d46fff',
        },
        '& .MuiPaginationItem-root:hover': {
          backgroundColor: 'rgba(212,111,255,0.2)', 
        },
      }}
      renderItem={(item) => (
        <PaginationItem
          slots={{ previous: ArrowBackIcon, next: ArrowForwardIcon }}
          {...item}
        />
      )}
    />
  );
}
